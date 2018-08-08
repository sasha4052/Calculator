import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class Calculator {
    private static Schema schema;
    static{
         File schemaLocation = new File("Calculator.xsd");
         SchemaFactory schemfactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         try {
             schema = schemfactory.newSchema(schemaLocation);
         }catch(SAXException e){

         }
    }
   private static  boolean validateXml(XMLStreamReader reader) throws XMLStreamException {
       Validator validator = schema.newValidator();
       try {
           validator.validate(new StAXSource(reader));
       } catch (Exception e){
           return false;
       }
       finally {
           reader.close();
       }
    return true ;
   }

   public static void calculate(String filePath){
      XMLStreamReader reader = null;
      try {
        if(validateXml(XMLInputFactory.newInstance().createXMLStreamReader(filePath, new FileInputStream(filePath)))){
            reader  = XMLInputFactory.newInstance().createXMLStreamReader(filePath, new FileInputStream(filePath));
            Stack<String> stackElements = new Stack<>();
            List<Double> listOpernads = new LinkedList<>();
            Pattern p = Pattern.compile(".+\\.(xsd|xml)");
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement() && reader.getAttributeCount()>0 && !p.matcher(reader.getAttributeValue(0)).matches()) {
                     stackElements.push(reader.getAttributeValue(0));
                } else if (reader.isEndElement() && !reader.getLocalName().equals("arg")&& !reader.getLocalName().equals("expression") && !reader.getLocalName().equals("simpleCalculator")&& !reader.getLocalName().equals("expressions") ) {
                    boolean isStartOperation = false;
                    String operand;
                    while(!isStartOperation && !stackElements.empty()){
                        operand = stackElements.pop();
                        switch (operand){
                            case "MUL":
                                mulOperation(stackElements,listOpernads );
                                isStartOperation=!isStartOperation;
                                System.out.println("mul-stack:"+ stackElements);
                                System.out.println("mul-list:"+ listOpernads);
                                break;
                            case "SUB" :
                                subOperation(stackElements,listOpernads );
                                isStartOperation=!isStartOperation;
                                System.out.println("sub-stack:"+ stackElements);
                                System.out.println("sub-list:"+ listOpernads);
                                break;
                            case "SUM":
                                sumOperation(stackElements,listOpernads );
                                isStartOperation=!isStartOperation;
                                System.out.println("sum-stack:"+ stackElements);
                                System.out.println("sum-list:"+ listOpernads);
                                break;
                            case "DIV" :
                                divOperation(stackElements,listOpernads );
                                isStartOperation=!isStartOperation;
                                System.out.println("div-stack:"+ stackElements);
                                System.out.println("div-list:"+ listOpernads);
                                break;
                            default:
                                listOpernads.add(Double.valueOf(operand));
                                break;
                        }
                    }
                } else if (reader.hasText() && reader.getText().trim().length() > 0) {
                    stackElements.push(reader.getText());
                }
            }
            reader.close();
            System.out.println("result: "+ stackElements);
            generteResult(stackElements);
        } else {
            String message ="Invalid xml file format!";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        };
       } catch (FileNotFoundException e){
          String message ="xml file not found!";
          JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                  JOptionPane.ERROR_MESSAGE);
       } catch (XMLStreamException e){
          String message ="Unknown Error!";
          JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                  JOptionPane.ERROR_MESSAGE);
       }

   }

   private static void generteResult(Stack<String> result){
       Collections.reverse(result);
       try {
           XMLOutputFactory output = XMLOutputFactory.newInstance();
           XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter("result.xml"));

           writer.writeStartDocument("1.0");
           writer.writeStartElement("simpleCalculator");
           writer.writeStartElement("expressionResults");
           while (!result.empty()) {
               writer.writeStartElement("expressionResult");
               writer.writeStartElement("result");
               writer.writeCharacters(result.pop());
               writer.writeEndElement();
               writer.writeEndElement();

           }
           writer.writeEndElement();
           writer.writeEndElement();
           writer.writeEndDocument();
           writer.flush();
       } catch (XMLStreamException | IOException ex) {
           ex.printStackTrace();
       }
   }

    private static void mulOperation(Stack<String> stackElements,  List<Double> listOpernads){
       stackElements.push(""+(listOpernads.stream().reduce((x,y)->x*y).get()));
       listOpernads.clear();
   };

    private static void sumOperation(Stack<String> stackElements,  List<Double> listOpernads){
       stackElements.push(""+(listOpernads.stream().reduce((x,y)->x+y).get()));
       listOpernads.clear();
    };

    private static void subOperation(Stack<String> stackElements,  List<Double> listOpernads){
       Collections.reverse(listOpernads);
       stackElements.push(""+(listOpernads.stream().reduce((x,y)->x-y).get()));
       listOpernads.clear();
   };

    private static void divOperation(Stack<String> stackElements,  List<Double> listOpernads){
       Collections.reverse(listOpernads);
       stackElements.push(""+(listOpernads.stream().reduce((x,y)->x/y).get()));
       listOpernads.clear();
   }

}
