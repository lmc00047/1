//***********************************************************************
//
//   GUAJE: Generating Understandable and Accurate Fuzzy Models in a Java Environment
//
//   Contact: guajefuzzy@gmail.com
//
//   Copyright (C) 2007 - 2015  Jose Maria Alonso Moral
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program. If not, see <http://www.gnu.org/licenses/>.
//
//***********************************************************************

//***********************************************************************
//
//
//                            XMLParser.java
//
//
//**********************************************************************

package xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * xml.XMLParser is a parser for XML files
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class XMLParser {

  public XMLParser() {}

  public Object getXMLinfo (String xmlFileName, String option) {
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      SAXParser saxParser = spf.newSAXParser();
      XMLReader parser = saxParser.getXMLReader();
      // create a handler
      XMLParserHandler handler = new XMLParserHandler();
      // assign the handler to the parser
      parser.setContentHandler(handler);
      // parse the document
      if (xmlFileName.contains("Documents and Settings"))
          xmlFileName= xmlFileName.replace("Documents and Settings","Docume~1");
      //System.out.println("PARSER: "+xmlFileName);
      parser.parse(xmlFileName);
      if (option.equals("ExpertFile"))
          return handler.ExpertFile;
      else if (option.equals("FISoperators"))
	      return handler.fisOperators;
      else if (option.equals("DataInfo"))
	      return handler.dataInfo;
      else if (option.equals("VarInfo"))
	      return handler.varInfo;
      else if (option.equals("ContextInfo"))
	      return handler.contextInfo;
      else if (option.equals("InterpretabilityInfo"))
	      return handler.interpretabilityInfo;
      else if (option.equals("OntologyInfo"))
	      return handler.ontologyInfo;
      else if (option.equals("SystemInfo"))
	      return handler.systemInfo;
      else if (option.equals("SystemInputs"))
	      return handler.systemInputs;
      else if (option.equals("SystemInputLabels"))
	      return handler.systemInputLabels;
      else if (option.equals("SystemOutputs"))
	      return handler.systemOutputs;
      else if (option.equals("SystemOutputLabels"))
	      return handler.systemOutputLabels;
      else if (option.equals("SystemRules"))
	      return handler.systemRules;
      else if (option.equals("AutoConfParameters"))
	      return handler.confParameters;
    }
    catch (ParserConfigurationException e) {
      System.out.println("Error in configuration of SAXParser.");
      e.printStackTrace();
    }
    catch (IOException e) {
      System.out.println("Error in configuration of input-output in XMLParser.");
      e.printStackTrace();
    }
    catch (SAXException e) {
      System.out.println("Error in XMLParser.");
      e.printStackTrace();
    }
    return null;
  }
}