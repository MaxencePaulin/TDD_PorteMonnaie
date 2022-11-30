package izly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

public class CodeSecretUnitTest {

    private CodeSecret codeSecret;
    private String goodCode = "9876";
    private String badCode = "1234";

    @BeforeEach
    public void setup() {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(10)).thenReturn(9, 8, 7, 6);
        codeSecret = CodeSecret.createCodeSecret(random);
    }

    @Test
    public void testGenerationAleatoireDuCodeSecretEtRevelerCode() {
//        Assertions.assertTrue(isCode(codeSecret.revelerCode()));
        Assertions.assertEquals("9876", codeSecret.revelerCode());
        Assertions.assertEquals("xxxx", codeSecret.revelerCode());
        Assertions.assertEquals("xxxx", codeSecret.revelerCode());
    }

//    private boolean isCode(String code) {
////        return code.matches("[0-9]{4}");
//        if (code.length() != 4) return false;
//        for (int i = 0; i < code.length(); i++) {
//            if (code.charAt(i)<'0' || code.charAt(i)>'9') return false;
//        }
//        return true;
//    }

    @Test
    public void testVerifierCode() {
        Assertions.assertTrue(codeSecret.verifierCode(goodCode));
        Assertions.assertFalse(codeSecret.verifierCode(badCode));
    }

    @Test
    public void testCodeBloqueApresTroisEssaisFauxSuccessifs() {
        codeSecret.verifierCode(badCode);
        codeSecret.verifierCode(badCode);
        Assertions.assertFalse(codeSecret.isCodeBloque());
        codeSecret.verifierCode(badCode);
        Assertions.assertTrue(codeSecret.isCodeBloque());
    }

    @Test
    public void testNbEssaisReinitialiseApresSaisieDunCodeJusteAvantLeTroisiemeEssai() {
        codeSecret.verifierCode(badCode);
        codeSecret.verifierCode(badCode);
        codeSecret.verifierCode(goodCode);
        Assertions.assertFalse(codeSecret.isCodeBloque());
        codeSecret.verifierCode(badCode);
        Assertions.assertFalse(codeSecret.isCodeBloque());
        codeSecret.verifierCode(badCode);
        Assertions.assertFalse(codeSecret.isCodeBloque());
        codeSecret.verifierCode(badCode);
        Assertions.assertTrue(codeSecret.isCodeBloque());
    }

    @Test
    public void testNbEssaiNonReinitialiseApresTroisEssaisFauxSuccessifsAyantBloqueLeCodeDefinitivement() {
        codeSecret.verifierCode(badCode);
        codeSecret.verifierCode(badCode);
        codeSecret.verifierCode(badCode);
        Assertions.assertTrue(codeSecret.isCodeBloque());
        Assertions.assertFalse(codeSecret.verifierCode(goodCode));
        Assertions.assertTrue(codeSecret.isCodeBloque());
    }
}
