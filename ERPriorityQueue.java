import java.util.ArrayList;
import java.util.HashMap;

public class ERPriorityQueue {

	public ArrayList<Patient> patients;
	public HashMap<String, Integer> nameToIndex;

	public ERPriorityQueue() {

		//  use a dummy node so that indexing starts at 1, not 0

		patients = new ArrayList<Patient>();
		patients.add(new Patient("dummy", 0.0));
		nameToIndex = new HashMap<String, Integer>();
	}

	private int parent(int i) {
		return i / 2;
	}

	private int leftChild(int i) {
		return 2 * i;
	}

	private int rightChild(int i) {
		return 2 * i + 1;
	}

	private boolean isLeaf(int i) {
		return (!hasLeft(i) && !hasRight(i));
	}

	private boolean hasLeft(int i) {
		return leftChild(i) < patients.size();
	}

	private boolean hasRight(int i) {
		return rightChild(i) < patients.size();
	}

	private boolean isEmpty() {
		if (patients.size() == 1)
			return true;
		return false;
	}

	private void swap(int i, int j) {
		// HashMap
		String name1 = patients.get(i).getName();
		String name2 = patients.get(j).getName();
		int temp = nameToIndex.get(name1);
		nameToIndex.put(name1, nameToIndex.get(name2));
		nameToIndex.put(name2, temp);

		// ArrayList
		Patient tmp = patients.get(i);
		patients.set(i, patients.get(j));
		patients.set(j, tmp);
	}

	private int getIndex(String name) {
		// return the index of a name in patients
		for (int i = 1; i < patients.size(); i++) {
			String currentName = patients.get(i).getName();
			if (currentName.equals(name))
				return i;
		}
		return -1; // return -1 when the name is not found
	}

	public void upHeap(int i) {
		while (i > 1 && patients.get(i).compareTo(patients.get(parent(i))) < 0) {
			swap(i, parent(i));
			i = parent(i);
		}
	}

	public void downHeap(int i) {
		if (isLeaf(i))
			return;
		while (hasLeft(i)) {
			int child = leftChild(i);
			if (child < patients.size() - 1) {
				if (hasRight(i)
						&& patients.get(rightChild(i)).getPriority() < patients.get(leftChild(i)).getPriority()) {
					child++; // change to right
				}
			}
			if (patients.get(child).getPriority() < patients.get(i).getPriority()) {
				swap(i, child);
				i = child;
			} 
			else
				return;
		}
	}
	
	public boolean legalHeap() {
        for (int i = 1; i <= (patients.size() - 1)/2; i++) {
            if (patients.get(i).priority > patients.get(i * 2).priority) return false;
            if (patients.size() - 1 >= i * 2 + 1 && patients.get(i).priority > patients.get(i * 2 + 1).priority) return false;        
        }
        return true;
    }

	public boolean contains(String name) {
		if (isEmpty())
			return false;
		return nameToIndex.containsKey(name);
	}

	public double getPriority(String name) {
		if (nameToIndex.isEmpty())
			return -1;
		return patients.get(nameToIndex.get(name)).getPriority();
	}

	public double getMinPriority() {
		if (isEmpty())
			return -1;
		return patients.get(1).getPriority();
	}

	public String removeMin() {
		if (isEmpty())
			return null;
		Patient removed = patients.get(1);
		if (patients.size() == 2) { // if after removing there's only dummy element
			patients.remove(patients.size() - 1);
			nameToIndex.remove(removed.getName());
			return removed.getName();
		}
		swap(1, patients.size() - 1);
		patients.remove(patients.size() - 1);
		nameToIndex.remove(removed.getName());
		downHeap(1);
		return removed.getName();
	}

	public String peekMin() {
		if (isEmpty())
			return null;
		return patients.get(1).getName();
	}

	/*
	 * There are two add methods.  The first assumes a specific priority.
	 * The second gives a default priority of Double.POSITIVE_INFINITY
	 *
	 * If the name is already there, then return false.
	 */

	public boolean add(String name, double priority) {
		if (contains(name)) // check if the patient is already added
			return false;
		patients.add(new Patient(name, priority));
		int lastIndex = patients.size() - 1;
		nameToIndex.put(name, lastIndex); // put in hashmap
		upHeap(lastIndex); // upHeap the added entry
		return true;
	}

	public boolean add(String name) {
		if (contains(name)) // check if the patient is already added
			return false;
		patients.add(new Patient(name, Double.POSITIVE_INFINITY)); // add patient
		nameToIndex.put(name, patients.size() - 1); // put in hashmap
		return true;
	}

	public boolean remove(String name) {
		if (!contains(name)) // check if the patient is in list
			return false;
		if (patients.size() == 2) { // check if after removing there's only dummy element
			patients.remove(1);
			nameToIndex.remove(name);
			return true;
		}
		int i = getIndex(name);
		swap(i, patients.size() - 1);
		patients.remove(patients.size() - 1);
		nameToIndex.remove(name);
		downHeap(i);
		return true;
	}

	/*
	 *   If new priority is different from the current priority then change the priority
	 *   (and possibly modify the heap).
	 *   If the name is not there, return false
	 */

	public boolean changePriority(String name, double newPriority) {
		if (!contains(name))
			return false;
		int i = nameToIndex.get(name);
		Patient current = patients.get(i);
		double oldPriority = current.getPriority();
		if (oldPriority == newPriority) {
			return true;
		}

		current.setPriority(newPriority);
		if (oldPriority > newPriority) {
			upHeap(i);
		}
		else {
			downHeap(i);
		}
		return true;
	}

	public ArrayList<Patient> removeUrgentPatients(double threshold) {
		ArrayList<Patient> removedPatients = new ArrayList<Patient>();
		while (!isEmpty()) {
			int i = 1;
			Patient current = patients.get(i);
			if (current.getPriority() <= threshold) {
				removedPatients.add(current);
				remove(current.getName());
				nameToIndex.remove(current.getName());
			}
			if (current.getPriority() > threshold)
				break;
		}
		return removedPatients;
	}

	public ArrayList<Patient> removeNonUrgentPatients(double threshold) {
		ArrayList<Patient> removedPatients = new ArrayList<Patient>();
		int i = 1;
		while (!isEmpty() || i < patients.size()) {
			if (i == patients.size()) {
				break;
			}
			Patient current = patients.get(i);
			if (current.getPriority() >= threshold) {
				removedPatients.add(current);
				remove(current.getName());
				nameToIndex.remove(current.getName());
				i = 0;
			}
			i++;
		}
		return removedPatients;
	}

	static class Patient implements Comparable<Patient> {
		private String name;
		private double priority;

		Patient(String name, double priority) {
			this.name = name;
			this.priority = priority;
		}

		Patient(Patient otherPatient) {
			this.name = otherPatient.name;
			this.priority = otherPatient.priority;
		}

		double getPriority() {
			return this.priority;
		}

		void setPriority(double priority) {
			this.priority = priority;
		}

		String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name + " - " + this.priority;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof ERPriorityQueue.Patient))
				return false;
			Patient otherPatient = (Patient) obj;
			return this.name.equals(otherPatient.name) && this.priority == otherPatient.priority;
		}

		@Override
		public int compareTo(ERPriorityQueue.Patient o) {
			if (this.priority > o.priority)
				return 1;
			else if (this.priority == o.priority)
				return 0;
			else
				return -1;
		}
	}
}