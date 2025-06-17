package javaapplication131;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class User {
    protected String id;
    protected String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

    public abstract String getRole();
}

class Student extends User {
    public Student(String id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() {
        return "Student";
    }
}

class Instructor extends User {
    public Instructor(String id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() {
        return "Instructor";
    }
}

class Course {
    private String code;
    private String name;
    private User instructor;
    private List<User> students;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
        this.students = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setInstructor(User instructor) {
        if (instructor.getRole().equals("Instructor")) {
            this.instructor = instructor;
        } else {
            throw new IllegalArgumentException("Only Instructors can be assigned to teach a course.");
        }
    }

    public User getInstructor() {
        return instructor;
    }

    public void addStudent(User student) {
        if (student.getRole().equals("Student")) {
            students.add(student);
        } else {
            throw new IllegalArgumentException("Only Students can be enrolled in a course.");
        }
    }

    public List<User> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}

class VirtualClassroom {
    private Course course;
    private JFrame classFrame;
    private JTextArea chatArea;
    private JTextField chatInput;
    private JPanel whiteboard;
    private boolean isAudioOn = false;
    private boolean isVideoOn = false;

    public VirtualClassroom(Course course) {
        this.course = course;
    }

    public void startClass() {
        SwingUtilities.invokeLater(() -> {
            classFrame = new JFrame("Virtual Class: " + course.getName());
            classFrame.setSize(800, 600);
            classFrame.setLayout(new BorderLayout());

            JPanel contentPanel = new JPanel(new BorderLayout());

            whiteboard = new JPanel();
            whiteboard.setBackground(Color.WHITE);
            whiteboard.setBorder(BorderFactory.createTitledBorder("Whiteboard"));
            contentPanel.add(whiteboard, BorderLayout.CENTER);

            JPanel chatPanel = createChatPanel();
            contentPanel.add(chatPanel, BorderLayout.EAST);

            JPanel controlPanel = createControlPanel();
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            classFrame.add(contentPanel, BorderLayout.CENTER);

            JPanel participantsPanel = createParticipantsPanel();
            classFrame.add(participantsPanel, BorderLayout.WEST);

            classFrame.setVisible(true);
        });
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        chatPanel.setPreferredSize(new Dimension(200, 0));
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        chatInput = new JTextField();
        chatInput.addActionListener(e -> {
            String message = chatInput.getText();
            if (!message.isEmpty()) {
                chatArea.append("You: " + message + "\n");
                chatInput.setText("");
            }
        });
        chatPanel.add(chatInput, BorderLayout.SOUTH);

        return chatPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton audioButton = new JButton("Audio: OFF");
        audioButton.addActionListener(e -> {
            isAudioOn = !isAudioOn;
            audioButton.setText("Audio: " + (isAudioOn ? "ON" : "OFF"));
        });
        controlPanel.add(audioButton);

        JButton videoButton = new JButton("Video: OFF");
        videoButton.addActionListener(e -> {
            isVideoOn = !isVideoOn;
            videoButton.setText("Video: " + (isVideoOn ? "ON" : "OFF"));
        });
        controlPanel.add(videoButton);

        JButton shareScreenButton = new JButton("Share Screen");
        shareScreenButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(classFrame, "Screen sharing started (simulated)");
        });
        controlPanel.add(shareScreenButton);

        JButton raiseHandButton = new JButton("Raise Hand");
        raiseHandButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(classFrame, "You raised your hand");
        });
        controlPanel.add(raiseHandButton);

        return controlPanel;
    }

    private JPanel createParticipantsPanel() {
        JPanel participantsPanel = new JPanel();
        participantsPanel.setLayout(new BoxLayout(participantsPanel, BoxLayout.Y_AXIS));
        participantsPanel.setBorder(BorderFactory.createTitledBorder("Participants"));
        participantsPanel.setPreferredSize(new Dimension(150, 0));

        JLabel instructorLabel = new JLabel("Instructor: " + course.getInstructor().getName());
        participantsPanel.add(instructorLabel);

        participantsPanel.add(Box.createVerticalStrut(10));

        JLabel studentsLabel = new JLabel("Students:");
        participantsPanel.add(studentsLabel);

        for (User student : course.getStudents()) {
            JLabel studentLabel = new JLabel("- " + student.getName());
            participantsPanel.add(studentLabel);
        }

        return participantsPanel;
    }
}

public class VirtualUniversitySystem {
    private List<User> users = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    private JFrame frame;
    private JTextArea outputArea;

    public VirtualUniversitySystem() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Virtual University System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JButton addStudentButton = new JButton("Add Student");
        JButton addCourseButton = new JButton("Add Course");
        JButton addInstructorButton = new JButton("Add Instructor");
        JButton assignInstructorButton = new JButton("Assign Instructor to Course");
        JButton enrollStudentButton = new JButton("Enroll Student in Course");
        JButton startClassButton = new JButton("Start Virtual Class");

        panel.add(addStudentButton);
        panel.add(addCourseButton);
        panel.add(addInstructorButton);
        panel.add(assignInstructorButton);
        panel.add(enrollStudentButton);
        panel.add(startClassButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        addStudentButton.addActionListener(e -> addUser("Student"));
        addCourseButton.addActionListener(e -> addCourse());
        addInstructorButton.addActionListener(e -> addUser("Instructor"));
        assignInstructorButton.addActionListener(e -> assignInstructor());
        enrollStudentButton.addActionListener(e -> enrollStudent());
        startClassButton.addActionListener(e -> startVirtualClass());

        frame.setVisible(true);
    }

    private void addUser(String role) {
        String id = JOptionPane.showInputDialog("Enter " + role + " ID:");
        String name = JOptionPane.showInputDialog("Enter " + role + " name:");
        User user = role.equals("Student") ? new Student(id, name) : new Instructor(id, name);
        users.add(user);
        updateOutput(role + " added: " + name);
    }

    private void addCourse() {
        String code = JOptionPane.showInputDialog("Enter course code:");
        String name = JOptionPane.showInputDialog("Enter course name:");
        courses.add(new Course(code, name));
        updateOutput("Course added: " + name);
    }

    private void assignInstructor() {
        User instructor = (User) JOptionPane.showInputDialog(frame, "Select an instructor:",
                "Assign Instructor", JOptionPane.QUESTION_MESSAGE, null,
                users.stream().filter(u -> u.getRole().equals("Instructor")).toArray(), null);
        Course course = (Course) JOptionPane.showInputDialog(frame, "Select a course:",
                "Assign Instructor", JOptionPane.QUESTION_MESSAGE, null,
                courses.toArray(), null);
        if (instructor != null && course != null) {
            course.setInstructor(instructor);
            updateOutput("Assigned " + instructor.getName() + " to " + course.getName());
        }
    }

    private void enrollStudent() {
        User student = (User) JOptionPane.showInputDialog(frame, "Select a student:",
                "Enroll Student", JOptionPane.QUESTION_MESSAGE, null,
                users.stream().filter(u -> u.getRole().equals("Student")).toArray(), null);
        Course course = (Course) JOptionPane.showInputDialog(frame, "Select a course:",
                "Enroll Student", JOptionPane.QUESTION_MESSAGE, null,
                courses.toArray(), null);
        if (student != null && course != null) {
            course.addStudent(student);
            updateOutput("Enrolled " + student.getName() + " in " + course.getName());
        }
    }

    private void startVirtualClass() {
        Course course = (Course) JOptionPane.showInputDialog(frame, "Select a course to start:",
                "Start Virtual Class", JOptionPane.QUESTION_MESSAGE, null,
                courses.toArray(), null);
        if (course != null) {
            VirtualClassroom virtualClassroom = new VirtualClassroom(course);
            virtualClassroom.startClass();
            updateOutput("Started virtual class for " + course.getName());
        }
    }

    private void updateOutput(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VirtualUniversitySystem::new);
    }
}