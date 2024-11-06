package store.view;

import store.constants.GuideMessage;

public class OutputView {

    public void printWelcomeMessage(){
        System.out.println(GuideMessage.WELCOME_MESSAGE.getMessage()+"\n");
    }
}
