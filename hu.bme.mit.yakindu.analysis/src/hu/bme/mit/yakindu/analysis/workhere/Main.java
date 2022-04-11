package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Trigger;
import org.yakindu.sct.model.sgraph.Vertex;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		generateJavaCode(new String[0]);
	}
	
	public static void generateJavaCode(String[] args) {

		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<EventDefinition> events = new ArrayList<EventDefinition>();
		ArrayList<VariableDefinition> variables = new ArrayList<VariableDefinition>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				events.add((EventDefinition) content);
			}
			if(content instanceof VariableDefinition) {
				variables.add((VariableDefinition) content);
			}
			
		}
		
		String output="ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"s.setTimer(new TimerService());\r\n" + 
				"RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"s.init();\r\n" + 
				"s.enter();\r\n" + 
				"\r\n" + 
				"boolean exit=false;\r\n" + 
				"do{\r\n" + 
				"	String input=reader.readLine();\r\n" + 
				"	switch(input) {\r\n";
		for(EventDefinition e: events) {
			output+="case \""+e.getName()+"\":\r\n";
			output+="s.raise"+e.getName()+"();\r\n";
			output+="s.runCycle();\r\n";
			output+="break;\r\n";
		}
		output+="case \"exit\":\r\n" + 
				"exit=true;\r\n" + 
				"break;"+
				"default: \r\n break;\r\n" + 
				"	}\r\n";
		for(VariableDefinition v: variables) {
			output+="System.out.println(\""+v.getName()+"= \"+ s.getSCInterface().get"+v.getName()+"());\r\n";
		}
		output+="}while (!exit); \r\n" + 
				"System.exit(0);";
		System.out.println(output);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<State> states = new ArrayList<State>();
		ArrayList<State> trapStates = new ArrayList<State>();
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		ArrayList<EventDefinition> events = new ArrayList<EventDefinition>();
		ArrayList<VariableDefinition> variables = new ArrayList<VariableDefinition>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				states.add(state);
			}
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				transitions.add(transition);
			}
			if(content instanceof EventDefinition) {
				events.add((EventDefinition) content);
			}
			if(content instanceof VariableDefinition) {
				variables.add((VariableDefinition) content);
			}
			
		}
		
		
		for(State s1: states) {
			boolean trapState=true;		
			System.out.println(s1.getName());
			for(Transition t: transitions) {
				if(t.getSource().equals(s1)) {
					trapState=false;
				}
			}
			if(trapState) {
				trapStates.add(s1);
			}
		}

		for(Transition t2: transitions) {
			System.out.println(t2.getSource().getName() + "->"+t2.getTarget().getName());
			
		}
		System.out.println("Trapstates:");
	
		for(State s4: trapStates) {
			System.out.println(s4.getName());
		}
		System.out.println("public static void print(IExampleStatemachine s) {"); 
		for(EventDefinition e: events) {
			System.out.println("System.out.println("+e.getName()+"= \" + s.getSCInterface().get"+e.getName()+"());");
			//System.out.println(e.getName());
		}
		
		for(VariableDefinition v: variables) {
			System.out.println("System.out.println("+v.getName()+"= \" + s.getSCInterface().get"+v.getName()+"());");
			//System.out.println(v.getName());
		}
System.out.println("}");
		
		

		int counter=1;
		for(State s4: states) {
			if(s4.getName()=="") {
				System.out.println("this has no name"+s4);
				System.out.println("name could be: tempname"+(counter));
				counter++;
			}
		}
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}

}
