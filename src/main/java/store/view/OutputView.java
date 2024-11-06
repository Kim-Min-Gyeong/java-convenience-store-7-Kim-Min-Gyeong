package store.view;

import store.constant.GuideMessage;
import store.model.Product;

import java.util.List;

public class OutputView {

    public void printWelcomeMessage(){
        System.out.println(GuideMessage.WELCOME_MESSAGE.getMessage()+GuideMessage.NEXT_LINE.getMessage());
    }

    public void printProductInfo(List<Product> products){
        for(Product product: products){
            System.out.println(product.toString());
        }
    }

    public void printInputMessage(){
        System.out.println(GuideMessage.NEXT_LINE.getMessage()+GuideMessage.PRODUCT_NAME_QUANTITY_MESSAGE.getMessage());
    }
}
