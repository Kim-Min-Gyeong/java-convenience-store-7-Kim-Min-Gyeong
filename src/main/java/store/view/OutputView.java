package store.view;

import store.constant.GuideMessages;
import store.constant.Receipt;
import store.model.Consumer;
import store.model.Gift;
import store.model.Inventory;
import store.model.PurchaseProduct;

import java.text.DecimalFormat;
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
        System.out.println(GuideMessages.NEXT_LINE.getMessage() + GuideMessages.PRODUCT_NAME_QUANTITY_MESSAGE.getMessage());
    }

    public void printOneMoreMessage(String name){
        System.out.println();
        System.out.format(GuideMessages.ONE_MORE_MESSAGE.getMessage(), name);
        System.out.println();
    }

    public void printWithoutPromotionMessage(String name, Integer quantity){
        System.out.println();
        System.out.format(GuideMessages.WITHOUT_PROMOTION.getMessage(), name, quantity);
    }

    public void printMemberShipMessage(){
        System.out.println();
        System.out.println(GuideMessages.MEMBERSHIP_MESSAGE.getMessage());
    }

    public void printReceipt(Consumer consumer){
        System.out.println();
        System.out.println(Receipt.HEADER.getText());
        System.out.print(Receipt.PRODUCT_TITLE.getText());
        printProduct(consumer);
        System.out.println(Receipt.GIFT_TITLE.getText());
        printGift(consumer);
        System.out.println(Receipt.SEPARATOR.getText());
        System.out.format(Receipt.TOTAL_AMOUNT.getText(), consumer.getTotalQty(), convertNumber(consumer.getTotalAmount()));
        System.out.format(Receipt.PROMOTION_DISCOUNT.getText(), convertNumber(consumer.getEventDiscount()));
        System.out.format(Receipt.MEMBERSHIP_DISCOUNT.getText(), convertNumber(consumer.getMembershipDiscount()));
        System.out.format(Receipt.FINAL_PAYMENT.getText(), convertNumber(consumer.getAmountToPay()));
    }

    private void printProduct(Consumer consumer){
        List<PurchaseProduct> purchaseProducts = consumer.getPurchaseProducts();
        for(PurchaseProduct pp: purchaseProducts){
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedNumber = decimalFormat.format((long) pp.getQuantity() * pp.getPrice());
            System.out.format(Receipt.PRODUCT.getText(), pp.getName(), pp.getQuantity(), formattedNumber);
        }
    }

    private void printGift(Consumer consumer){
        List<Gift> gifts = consumer.getGifts();
        for(Gift g: gifts){
            System.out.format(Receipt.GIFT.getText(), g.getName(), g.getQuantity(), "");
        }
    }

    private String convertNumber(Integer number){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format((int)number);
    }
}
