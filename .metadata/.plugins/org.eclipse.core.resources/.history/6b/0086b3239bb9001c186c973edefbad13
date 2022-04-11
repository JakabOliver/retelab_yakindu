package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
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
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				states.add(state);
				System.out.println(state.getName());
			}
		}
		TreeIterator<EObject> iterator2 = s.eAllContents();
		
		while (iterator2.hasNext()) {
			EObject content = iterator2.next();
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				System.out.println(transition.getSource().getName() + "->"+transition.getTarget().getName());
				transitions.add(transition);
			}
		}
		
		for(State s3: states) {
			boolean trapState=true;
			for(Transition t: transitions) {
				if(t.getSource().equals(s3)) {
					trapState=false;
				}
			}
			if(trapState) {
				trapStates.add(s3);
			}
		}
		for(State s4: trapStates) {
			System.out.println(s4.getName());
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
