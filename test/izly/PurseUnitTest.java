package izly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PurseUnitTest {

    private final String goodPinCode = "9876";
    private CodeSecret secretCode;
    private String codePin;
    private final int operationMax = 1000;

    @BeforeEach
    public void setup() throws Exception{
        secretCode = Mockito.mock(CodeSecret.class);
        Mockito.when(secretCode.verifierCode(goodPinCode)).thenReturn(true);
    }

    @Test
    public void testCredit() throws Exception {
        Purse purse = new Purse(operationMax, 100, secretCode);
        double solde = purse.getSolde();
        purse.credite(50);
        Assertions.assertEquals(solde + 50, purse.getSolde());
    }

    @Test
    public void testDebit() throws Exception {
        Purse purse = new Purse(operationMax, 100, secretCode);
        purse.credite(50);
        double solde = purse.getSolde();
        purse.debite(50, goodPinCode);
        Assertions.assertEquals(solde - 50, purse.getSolde());
    }

    @Test
    public void testSoldeToujoursPositifSurDebit() {
        Purse purse = new Purse(operationMax, 100, secretCode);
        double solde = purse.getSolde();
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(solde+1, goodPinCode));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof SoldeNegatifInterditException);
    }

    @Test
    public void testSoldeToujoursInferieurAuPlafondSurCredit() {
        double plafond = 100;
        Purse purse = new Purse(operationMax, plafond, secretCode);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.credite(plafond+1));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof DepassementPlafondInterditException);
    }

    @Test
    public void testDebitNegatifInterdit() throws Exception{
        Purse purse = new Purse(operationMax, 100, secretCode);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(-1, goodPinCode));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof MontantNegatifInterditException);
    }

    @Test
    public void testCreditNegatifInterdit() throws Exception{
        Purse purse = new Purse(operationMax, 100, secretCode);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(-1, goodPinCode));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof MontantNegatifInterditException);
    }

    @Test
    public void testDebitRejeteSurCodeFaux() throws Exception{
        CodeSecret codeSecret = Mockito.mock(CodeSecret.class);
        Mockito.when(codeSecret.verifierCode("1234")).thenReturn(false);
        Purse purse = new Purse(operationMax, 100, codeSecret);
        purse.credite(50);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(20, "1234"));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof CodePinFauxException);
    }

    @Test
    public void testDebitRejeteSurCodeBloque() throws Exception{
        CodeSecret codeSecret = Mockito.mock(CodeSecret.class);
        Mockito.when(codeSecret.isCodeBloque()).thenReturn(false, true);
        Purse purse = new Purse(operationMax, 100, codeSecret);
        purse.credite(50);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(20, goodPinCode));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof CodeBloqueException);
    }

    @Test
    public void testCreditRejeteSurCodeBloque() throws Exception{
        CodeSecret codeSecret = Mockito.mock(CodeSecret.class);
        Mockito.when(codeSecret.isCodeBloque()).thenReturn(true);
        Purse purse = new Purse(operationMax, 100, codeSecret);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.credite(20));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof CodeBloqueException);
    }

    @Test
    public void testDureeDeVieLimite() throws Exception {
        Purse purse = new Purse(4, 100, secretCode);
        purse.credite(70);
        purse.debite(20, goodPinCode);
        purse.credite(10);
        purse.debite(40, goodPinCode);
        OperationRejeteeException operationRejeteeException = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.debite(10, goodPinCode));
        Assertions.assertTrue(operationRejeteeException.getCause() instanceof NbOperationsMaxAtteintException);
        OperationRejeteeException operationRejeteeException2 = Assertions.assertThrows(OperationRejeteeException.class, () -> purse.credite(10));
        Assertions.assertTrue(operationRejeteeException2.getCause() instanceof NbOperationsMaxAtteintException);
    }
}
