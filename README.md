# Emergency-Room
Simulation of an Emergency Room using priority queue, hash map, and heaps

Imagine the emergency room (ER) of a hospital. Patients who come to the ER are assessed (triage). We
associate the urgency of the patient’s problem with a single value: the priority.2 As in the heap lectures, we
use a min heap to cater to patients with more urgent situations first. So three patients with priority values
say 3, 4, 8 would be seen in that order. We use double valued priorities since we want to have flexibility,
for example, to add a patient with priority 3.2 between patients with priorities 3 and 4. You should assume
the priorities are always be strictly greater than 0.
The ERPriorityQueue class allows us to organize (name, priority) pairs using nodes which are stored
in a heap. Each node in the heap is an instance of a Patient class which has two fields (name, priority).
The heap implementation will use an ArrayList of these Patients. The Patient class is given to you in the
starter code. For grading purposes, we have included a toString() and equals() method in the Patient class.
For technical reasons having to do with the grader, we have also made Patient a static inner class.
The ERPriorityQueue class has two fields: an ArrayList of Patient objects called patients and a
HashMap nameToIndex. Both of these fields are public for grading purposes. The HashMap nameToIndex is defined as follows: the key is a string and the value is the index of that string in the ArrayList.
This allows us to find a patient (given their name) in the priority queue.
