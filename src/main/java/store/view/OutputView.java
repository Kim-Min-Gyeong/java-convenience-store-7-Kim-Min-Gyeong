package store.view;

import store.constant.GuideMessages;
import store.model.Inventory;

import java.util.List;

public class OutputView {

    public void printWelcomeMessage(){
        System.out.println(GuideMessages.WELCOME_MESSAGE.getMessage()+ GuideMessages.NEXT_LINE.getMessage());
    }

    public void printProductInfo(List<Inventory> inventories){
        for(Inventory inventory : inventories){
            System.out.println(inventory.toString());
        }
    }

    public void printInputMessage(){
        System.out.println(GuideMessages.NEXT_LINE.getMessage()+ GuideMessages.PRODUCT_NAME_QUANTITY_MESSAGE.getMessage());
    }
}
