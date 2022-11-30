package izly;

import java.util.Random;

public class CodeSecret {

    private String code;
    private boolean firstCall;
    private int nbEssaisCode;
    private boolean codeBloque;

    public static CodeSecret createCodeSecret(Random random) {
        String code1 = "";
        for (int i = 0; i < 4; i++) {
            code1 += random.nextInt(10);
        }
        return new CodeSecret(code1);
    }

    private CodeSecret(String code) {
        this.firstCall = true;
        this.code = code;
        this.nbEssaisCode = 3;
        this.codeBloque = false;
    }

    public boolean verifierCode(String code) {
        if (codeBloque) return false;
        if (!this.code.equals(code)) {
            nbEssaisCode--;
            if (nbEssaisCode<=0) codeBloque=true;
            return false;
        }
        nbEssaisCode = 3;
        return true;
    }

    public boolean isCodeBloque() {
        return codeBloque;
    }

    public String revelerCode() {
        if(firstCall) {
            firstCall = false;
            return code;
        }
        return "xxxx";
    }
}
