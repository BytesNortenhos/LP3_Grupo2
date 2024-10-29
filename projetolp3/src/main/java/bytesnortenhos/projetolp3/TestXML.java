package bytesnortenhos.projetolp3;

import Utils.XMLUtils;

import java.sql.SQLException;

public class TestXML {
    public static void main() {
        XMLUtils xmlUtils = new XMLUtils();

        //-> Nomes Possíveis: athletes, sports, teams
        String testName = "sports";

        //-> Verifica se existem: <testName>.xml / <testName>_xsd.xml
        Boolean[] tempBoolean = xmlUtils.checkFilesExist(testName);
        if(!tempBoolean[0] || !tempBoolean[1]) System.out.println("[" + testName + "] > " + "XML: " + tempBoolean[0] + " / XSD: " + tempBoolean[1]);

        //-> Se o XML e XSD existirem:
        if(tempBoolean[0] && tempBoolean[1]) {

            //-> Verifica se o XML é válido através do XSD
            boolean tempValid = xmlUtils.validateXML(testName);
            if(!tempValid) System.out.println("[" + testName + "] > " + "XML Válido: " + tempValid);

            //-> Se o XML for válido:
            xmlUtils.getSportsDataXML();

            // Será preciso saber o tipo (athletes, sports, etc.) para chamar o metodo certo
            // para instanciar os objetos correspondentes e usar a DAO para meter os dados na BD
            // PS.: Para este commit, apenas será feito a apresentação dos dados
            //      apenas para a tarefa de guardar o conteúdo lido do XML para Base de dados
            //      é que será feito esses métodos
        }
    }
}
