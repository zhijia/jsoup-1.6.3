package org.jsoup.parser;

import org.jsoup.helper.DescendableLinkedList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan Hedley
 */
abstract class TreeBuilder {
    CharacterReader reader;
    Tokeniser tokeniser;
    protected Document doc; // current doc we are building into
    protected DescendableLinkedList<Element> stack; // the stack of open elements
    protected String baseUri; // current base uri, for creating new elements
    protected Token currentToken; // currentToken is used only for error tracking.
    protected ParseErrorList errors; // null when not tracking errors

    protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
        Validate.notNull(input, "String input must not be null");
        Validate.notNull(baseUri, "BaseURI must not be null");

        doc = new Document(baseUri);
        reader = new CharacterReader(input);
        this.errors = errors;
        tokeniser = new Tokeniser(reader, errors);
        stack = new DescendableLinkedList<Element>();
        this.baseUri = baseUri;
    }

    Document parse(String input, String baseUri) {
        return parse(input, baseUri, ParseErrorList.noTracking());
    }

    Document parse(String input, String baseUri, ParseErrorList errors) {
        initialiseParse(input, baseUri, errors);
        runParser();
        return doc;
    }

    static float total;
    static int count;

    
    protected void runParser() {
        // long tokenizationTime = 0;
        // long treebuildingTime = 0;
    	
        // FileWriter file = null;
        // try {
        // file = new FileWriter("tokens.txt");
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // PrintWriter out = new PrintWriter(file);
        // long sta = 0L;
        // long mid = 0L;
        // long end = 0L;
        while (true) {
        	
        	//sta = System.nanoTime();
            //System.out.println("TokenizerState: "+tokeniser.getState().toString());
			Token token = tokeniser.read();
			//mid = System.nanoTime();
			//System.out.println("TokenType     : "+token.type+"\t token: "+token);
			process(token);
			//end = System.nanoTime();

			//tokenizationTime += mid - sta;
			//treebuildingTime += end - mid;
            
            if (token.type == Token.TokenType.EOF)
                break;
        }
		//System.out.println("tokenization time: "+tokenizationTime);
		//System.out.println("treebuilding time: "+treebuildingTime);
        //out.close();
    }

    protected abstract boolean process(Token token);

    protected Element currentElement() {
        return stack.getLast();
    }
}
