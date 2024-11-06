package store.controller;

import store.view.OutputView;

public class ConvenienceStoreController {

    private final OutputView outputView;

    public ConvenienceStoreController(){
        this.outputView = new OutputView();
    }

    public void printWelcomeMessage(){ //환영 인사 출력
        outputView.printWelcomeMessage();
    }
}
