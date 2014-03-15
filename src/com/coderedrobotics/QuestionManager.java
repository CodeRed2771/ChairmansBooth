package com.coderedrobotics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Michael
 */
public class QuestionManager {

    private static final ArrayList<Question> questions = new ArrayList<>();
    Random random;
    int last = -1;
    
    public QuestionManager() {
        random = new Random();
    }

    public static void addQuestion(Question question) {
        questions.add(question);
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }
    
    public Question getRandomQuestion() {
        int i = random.nextInt(questions.size());
        return questions.get(i - 1);
    }
    
    public Question getNextQuestion(){
        last++;
        if (last > questions.size() - 1){
            last = 0;
        }
        return questions.get(last);
    }
}
