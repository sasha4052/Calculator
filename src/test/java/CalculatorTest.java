import org.junit.Assert;
import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;



public class CalculatorTest {

    List<String> getResulFormXml(){
        final String fileName = "result.xml";
        List<String> list = new LinkedList<>();
        try {
            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(fileName, new FileInputStream(fileName));
            while (xmlr.hasNext()) {
                xmlr.next();
                if (xmlr.hasText() && xmlr.getText().trim().length() > 0) {
                    System.out.println("   " + xmlr.getText());
                    list.add(xmlr.getText());
                }
            }
        } catch (FileNotFoundException | XMLStreamException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @org.junit.Test
    public void calculateSumTest() throws Exception {
        Calculator.calculate(".\\src\\test\\java\\testSum.xml");
        double result =  Double.valueOf(getResulFormXml().get(0));
        Assert.assertTrue(13-result == 0);

    }

    @org.junit.Test
    public void calculateSubTest() throws Exception {
        Calculator.calculate(".\\src\\test\\java\\testSub.xml");
        double result =  Double.valueOf(getResulFormXml().get(0));
        Assert.assertTrue(-3-result == 0);

    }

    @org.junit.Test
    public void calculateMulTest() throws Exception {
        Calculator.calculate(".\\src\\test\\java\\testMul.xml");
        double result =  Double.valueOf(getResulFormXml().get(0));
        Assert.assertTrue(24-result == 0);

    }

    @org.junit.Test
    public void calculateDivTest() throws Exception {
        Calculator.calculate(".\\src\\test\\java\\testDiv.xml");
        double result =  Double.valueOf(getResulFormXml().get(0));
        Assert.assertTrue(2.25-result == 0);

    }

    @org.junit.Test
    public void calculateSampleTest(){
        Calculator.calculate(".\\src\\test\\java\\sampleTest.xml");
        Object result[] = getResulFormXml().toArray();
        Object an[] = {"-2443.75", "59747.58686350021"};
        Assert.assertArrayEquals(an, result);
    }

}