//Jackie Zou
//Hangman project


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class MyFrame extends JFrame implements ActionListener {
    static List<Character> invalidLetters = new ArrayList<>();
    static List<Character> playerGuesses = new ArrayList<>();
    static List<Character> dashes = new ArrayList<>();
    static List<String> words = new ArrayList<>();
    static JLabel aLabel = new JLabel();
    static JLabel aLabel2 = new JLabel();
    static JLabel aLabel3 = new JLabel();
    static JFormattedTextField aTextField = new JFormattedTextField();
    static String hiddenWord;
    static char firstLetter;
    static String manyLetter;
    static int yesNO;
    static int healthPoints = 6;
    static int imageNumber = 0;
    static ImageIcon aImageIcon;

    static JButton aButton = new JButton();

    MyFrame() throws FileNotFoundException {

        //file selection
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        Scanner input = new Scanner(file);

        //add all words from file into this List
        while (input.hasNext()) {
            words.add(input.nextLine());
        }
        input.close();

        //creates a random number mechanic
        int random = (int) (Math.random() * words.size());
        hiddenWord = words.get(random).toLowerCase();

        //button
        aButton.setText("Cheat");
        aButton.setBounds(550, 205, 100, 50);
        aButton.addActionListener(this);
        this.add(aButton);
        //border
        Border aBorder = BorderFactory.createLineBorder(Color.GREEN, 3);

        //frame characteristics
        this.setTitle("Hangman: The game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(815, 840);
        this.getContentPane().setBackground(Color.black);
        this.setLayout(null);//manually move components


        //ImageIcon
        ImageIcon aImageIcon = new ImageIcon("Hangman" + imageNumber + ".png");
        aLabel.setIcon(aImageIcon);
        aLabel.setIconTextGap(200);
        ImageIcon logo = new ImageIcon("Hangman/logo.png");
        this.setIconImage(logo.getImage());


        //labels
        aLabel.setText("<html>Health:" + healthPoints + "<br/> Lets hangout...</html>");//set text
        aLabel.setForeground(Color.green);//set color of text
        aLabel.setFont(new Font("Consolas", Font.PLAIN, 30));//set font of text
        aLabel.setBorder(aBorder);
        aLabel.setHorizontalTextPosition(JLabel.CENTER);
        aLabel.setVerticalAlignment(JLabel.TOP);
        aLabel.setVerticalTextPosition(JLabel.TOP);
        aLabel.setBounds(0, 0, 400, 800);//sets x,y positions within as well as dimensions
        this.add(aLabel);
        aLabel2.setText("<html>Invalid Letters:<br/>" + invalidLetters.toString()//this format is to go to next line
                .replace(",", "")                                   //.replace is to make it look neater
                .replace("[", "")
                .replace("]", "") +
                "</html>");
        aLabel2.setForeground(Color.green);//set color of text
        aLabel2.setFont(new Font("Consolas", Font.PLAIN, 30));//set font of text
        aLabel2.setBorder(aBorder);
        aLabel2.setVerticalAlignment(JLabel.TOP); //move text up
        aLabel2.setHorizontalAlignment(JLabel.CENTER);
        aLabel2.setBounds(400, 0, 400, 200);//sets x,y positions within as well as dimensions
        this.add(aLabel2);
        //add dashes for each letter in the hiddenword as well as the size
        for (int i = 0; i < hiddenWord.length(); i++) {
            dashes.add('_');
        }
        updateALabel3();
        aLabel3.setForeground(Color.green);//set color of text
        aLabel3.setFont(new Font("Consolas", Font.PLAIN, 30));//set font of text
        aLabel3.setBorder(aBorder);
        aLabel3.setHorizontalAlignment(JLabel.CENTER);
        aLabel3.setVerticalAlignment(JLabel.BOTTOM);
        aLabel3.setBounds(400, 200, 400, 200);//sets x,y positions within as well as dimensions
        this.add(aLabel3);


        //text field
        aTextField.setBounds(400, 400, 400, 400);
        aTextField.setFont(new Font("Consolas", Font.PLAIN, 30));
        aTextField.setBorder(aBorder);
        aTextField.setForeground(Color.GREEN);
        aTextField.setBackground(Color.black);
        aTextField.addActionListener(this);//enables action listener
        aTextField.setHorizontalAlignment(JFormattedTextField.CENTER);
        aTextField.setText("[Delete me]");
        this.add(aTextField);
        //set visible
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == aButton) { //cheat button
            JOptionPane.showMessageDialog(null, hiddenWord, "cheat", JOptionPane.INFORMATION_MESSAGE);
        }
        start();
    }

    public static void start() { // game loop

        addInputToPlayerGuess(); //adds a char to a List called playerGuesses

        for (int i = 0; i < hiddenWord.length(); i++) {

            if (playerGuesses.get(0).equals(hiddenWord.charAt(i))) { //if theres any match
                if (manyLetter.equals(hiddenWord)) {//if the word matches hiddenword
                    for (int j = 0; j < hiddenWord.length(); j++) { //replace "_" with each correct letters
                        dashes.set(j, hiddenWord.charAt(j));//changes the "_" in our dash array to match a char from hiddenword

                    }

                } else {
                    dashes.set(i, hiddenWord.charAt(i));//if user typed one letter, replace all "_" in the correct position with the letter
                }
                updateALabel3();//refresh this part of gui

                if (compareAnswerToWord()) { // take dashes List and compares with a temp List that has the single hiddenword
                    winningDialog();//plays the situation if you won
                    break;//exit from loop since we won
                }
            }
            clearText();//clears text field after entering answer
        }
        if (!hiddenWord.contains(Character.toString(playerGuesses.get(0)))) { // if our guess is nowhere in the hiddenword
            if (healthPoints > 0 && !invalidLetters.contains(playerGuesses.get(0))) {// if we still have lives and if our letter is not a dupe.
                //we know if it's a dupe if we match it to invalidLetters, since invalid Letters has all letters that we guess, and not present in HiddenWord
                healthPoints--;//decrease health, health starts from 6 as declared in the global variable

                if (imageNumber < 6) { //we dont have image#7 so it stops at 6
                    imageNumber++;
                }

                storeWrongLetters(); //store invalidLetters
                updateALabel(); //refresh life and picture section
                updateALabel2(); //refresh Invalid Letters section
                clearText(); //clears textfield so we dont have to use backspace
            }
            if (healthPoints <= 0) {//when life is 0 we lose and plays the losing situation
                losingDialog();
            }

        }


    }

    public static void addInputToPlayerGuess() {
        manyLetter = aTextField.getText().toLowerCase();//word guess
        firstLetter = manyLetter.charAt(0);//letter guess
        playerGuesses.add(0, firstLetter);//List for letter only

    }

    public static void clearText() { //clears textfield
        aTextField.setText("");
    }

    public static void updateALabel3() { //refresh for dashes List
        aLabel3.setText(dashes.toString().replace(",", "")
                .replace("[", "")
                .replace("]", ""));
    }

    public static void updateALabel() {// refresh for pic/life count
        Random random = new Random();

        List<String> phrase = new ArrayList<>();
        phrase.add("Hang on...");
        phrase.add("Did you get the hang of it?");
        phrase.add("Winnable");
        phrase.add("This guy can hang!");
        phrase.add("What a grip...");

        //pick random phrase
        int randomNumber = random.nextInt(phrase.size());

        if (imageNumber >= 0 && imageNumber <= 6) {
            aLabel.setText("<html>Health:" + healthPoints + "<br/>" + phrase.get(randomNumber) + "</html>");//html format helps us go to next line
            aImageIcon = new ImageIcon("Hangman" + imageNumber + ".png");
            aLabel.setIcon(aImageIcon);
        }
    }

    public static void storeWrongLetters() {
        if (!invalidLetters.contains(playerGuesses.get(0))) { // no repeats
            invalidLetters.add(playerGuesses.get(0));
        }
    }

    public static void updateALabel2() {
        aLabel2.setText("<html>Invalid Letters:<br/>" + invalidLetters.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "") +
                "</html>");
    }

    public static void losingDialog() {

        yesNO = JOptionPane.showConfirmDialog(null, "you lost, would you like to try a new word?", "Loser :C", JOptionPane.YES_NO_OPTION);//yes = 0, no = 1

        if (yesNO == 0) {
            restart();
        } else {
            System.exit(0);
        }
    }

    public static void restart() {

        int random = (int) (Math.random() * words.size());
        hiddenWord = words.get(random).toLowerCase();
        healthPoints = 6;
        imageNumber = 0;
        aTextField.setText("[Delete me]");
        invalidLetters.clear();
        dashes.clear();
        for (int i = 0; i < hiddenWord.length(); i++) {
            dashes.add('_');
        }
        updateALabel();
        updateALabel2();
        updateALabel3();

    }

    public static void winningDialog() {

        yesNO = JOptionPane.showConfirmDialog(null, "you won, would you like to try a new word?", "Winner!!!", JOptionPane.YES_NO_OPTION);

        if (yesNO == 0) {
            restart();
        } else {
            System.exit(0);
        }
    }

    public static boolean compareAnswerToWord() {//method to see if our List match with the List with hiddenword
        List<Character> tempHiddenWord = new ArrayList<>();

        for (int i = 0; i < hiddenWord.length(); i++) {
            tempHiddenWord.add(i, hiddenWord.charAt(i));
        }

        if (dashes.equals(tempHiddenWord)) {
            return true;
        }
        return false;
    }

}