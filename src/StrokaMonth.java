public class StrokaMonth {

    String item_name;
    Boolean is_expense;
    int quantity;
    int sum_of_one;
    int value;

    StrokaMonth(String item_name, Boolean is_expense, int quantity, int sum_of_one) {
        this.item_name = item_name;
        this.is_expense = is_expense;
        this.quantity = quantity;
        this.sum_of_one = sum_of_one;
        this.value = this.sum_of_one*this.quantity;
    }



}


