package store.view;

import store.constant.GuideMessages;
import store.constant.Receipt;
import store.model.Consumer;
import store.model.Inventory;
import store.model.PurchaseProduct;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {

    private static final String THOUSAND_REGEX = "#,###";

    public void printWelcomeMessage() {
        System.out.println();
        System.out.println(GuideMessages.WELCOME_MESSAGE.getMessage() + GuideMessages.NEXT_LINE.getMessage());
    }

    public void printProductInfo(List<Inventory> inventories) {
        inventories.forEach(inventory -> System.out.println(inventory.toString()));
    }

    public void printInputMessage() {
        printFormattedMessage(GuideMessages.PRODUCT_NAME_QUANTITY_MESSAGE.getMessage());
    }

    public void printOneMoreMessage(String name) {
        printFormattedMessage(GuideMessages.ONE_MORE_MESSAGE.getMessage(), name);
    }

    public void printWithoutPromotionMessage(String name, Integer quantity) {
        printFormattedMessage(GuideMessages.WITHOUT_PROMOTION.getMessage(), name, quantity);
    }

    public void printMemberShipMessage() {
        printMessage(GuideMessages.MEMBERSHIP_MESSAGE.getMessage());
    }

    public void printReceipt(Consumer consumer) {
        System.out.println();
        printReceiptHeader();
        printProduct(consumer);
        printGift(consumer);
        printReceiptFooter(consumer);
    }

    private void printReceiptHeader() {
        System.out.println(Receipt.HEADER.getText());
        System.out.print(Receipt.PRODUCT_TITLE.getText());
    }

    private void printReceiptFooter(Consumer consumer) {
        System.out.println(Receipt.SEPARATOR.getText());
        printTotalAmount(consumer);
    }

    private void printTotalAmount(Consumer consumer) {
        System.out.format(Receipt.TOTAL_AMOUNT.getText(), consumer.getTotalQty(), formatNumber(consumer.getTotalAmount()));
        System.out.format(Receipt.PROMOTION_DISCOUNT.getText(), formatNumber(consumer.getEventDiscount()));
        System.out.format(Receipt.MEMBERSHIP_DISCOUNT.getText(), formatNumber(consumer.getMembershipDiscount()));
        System.out.format(Receipt.FINAL_PAYMENT.getText(), formatNumber(consumer.getAmountToPay()));
    }

    private void printProduct(Consumer consumer) {
        consumer.getPurchaseProducts().forEach(this::printProductDetails);
    }

    private void printProductDetails(PurchaseProduct pp) {
        String formattedNumber = formatNumber(pp.getQuantity() * pp.getPrice());
        System.out.format(Receipt.PRODUCT.getText(), pp.getName(), pp.getQuantity(), formattedNumber);
    }

    private void printGift(Consumer consumer) {
        if(consumer.getGifts().isEmpty()) return;
        System.out.println(Receipt.GIFT_TITLE.getText());
        consumer.getGifts().forEach(g -> System.out.format(Receipt.GIFT.getText(), g.getName(), g.getQuantity(), ""));
    }

    private String formatNumber(int number) {
        return new DecimalFormat(THOUSAND_REGEX).format(number);
    }

    private void printFormattedMessage(String message, Object... args) {
        System.out.println();
        System.out.format(message, args);
        System.out.println();
    }

    private void printMessage(String message) {
        System.out.println();
        System.out.println(message);
    }

    public void printAdditionalPurchase() {
        printMessage(GuideMessages.ADDITIONAL_PURCHASE.getMessage());
    }
}
