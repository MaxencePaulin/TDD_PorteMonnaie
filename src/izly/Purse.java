package izly;

import java.util.concurrent.Callable;

public class Purse {

    private double solde = 0;
    private double plafond;
    private CodeSecret codeSecret;

    private int nbOpMaxAuth;

    public Purse(int nbOpMaxAuth, double plafond, CodeSecret codeSecret) {
        this.nbOpMaxAuth = nbOpMaxAuth;
        this.plafond = plafond;
        this.codeSecret = codeSecret;
    }

    public double getSolde() {
        return solde;
    }

    public void credite(double montant) throws OperationRejeteeException {
        doOperation(montant, () -> {
            if (solde + montant > plafond) throw new DepassementPlafondInterditException();
            solde += montant;
            return null;
        });
    }

    public void debite(double montant, String codePin) throws OperationRejeteeException {
        doOperation(montant, () -> {
            if (!codeSecret.verifierCode(codePin)) throw new CodePinFauxException();
            if (montant > solde) throw new SoldeNegatifInterditException();
            solde -= montant;
            return null;
        });
    }

    private void doOperation(double montant, Callable codeOperation) throws OperationRejeteeException {
        if (isPurseBloque()) throw new OperationRejeteeException(new CodeBloqueException());
        if (nbOpMaxAuth <= 0) throw new OperationRejeteeException(new NbOperationsMaxAtteintException());
        if (montant < 0) throw new OperationRejeteeException(new MontantNegatifInterditException());
        try {
            codeOperation.call();
        } catch (Exception e) {
            throw new OperationRejeteeException(e);
        }
        nbOpMaxAuth--;
    }

    private boolean isPurseBloque() {
        return codeSecret.isCodeBloque();
    }
}


