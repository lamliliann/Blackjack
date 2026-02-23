import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class game {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter starting bank amount:");
        double bank = scan.nextDouble();

        boolean playing = true;

        while (playing && bank > 0) {

            System.out.println("\nCurrent Bank: $" + bank);
            System.out.println("Enter wager amount:");
            double wager = scan.nextDouble();

            if (wager > bank) {
                System.out.println("Not enough money.");
                continue;
            }

            bank -= wager;

            // Create and shuffle deck
            ArrayList<Integer> deck = new ArrayList<>();
            for (int i = 0; i < 52; i++) {
                deck.add(i);
            }
            Collections.shuffle(deck);

            List<Integer> playerHand = new ArrayList<>();
            List<Integer> dealerHand = new ArrayList<>();

            // Initial deal
            playerHand.add(deck.remove(0));
            dealerHand.add(deck.remove(0));
            playerHand.add(deck.remove(0));
            dealerHand.add(deck.remove(0));

            System.out.println("\nDealer shows:");
            printCard(dealerHand.get(0));
            System.out.println("Hidden card");

            System.out.println("\nYour cards:");
            for (int card : playerHand) {
                printCard(card);
            }

            int playerTotal = calculateScore(playerHand);
            int dealerTotal = calculateScore(dealerHand);

            // 🎯 Natural Blackjack check
            if (playerTotal == 21 && playerHand.size() == 2) {
                System.out.println("BLACKJACK!");

                if (dealerTotal == 21 && dealerHand.size() == 2) {
                    System.out.println("Push.");
                    bank += wager; // return bet
                } else {
                    System.out.println("You win 1.5x!");
                    bank += wager * 2.5; // original bet already removed
                }
                continue;
            }

            // ---------- PLAYER TURN ----------
            while (true) {

                System.out.println("Your total: " + playerTotal);

                if (playerTotal > 21) {
                    System.out.println("Bust! You lose.");
                    break;
                }

                System.out.println("Hit or Stay? (h/s)");
                String choice = scan.next();

                if (choice.equalsIgnoreCase("h")) {
                    playerHand.add(deck.remove(0));
                    printCard(playerHand.get(playerHand.size() - 1));
                    playerTotal = calculateScore(playerHand);
                } else {
                    break;
                }
            }

            if (playerTotal > 21) {
                continue; // lose wager
            }

            // ---------- DEALER TURN ----------
            System.out.println("\nDealer reveals:");
            for (int card : dealerHand) {
                printCard(card);
            }

            dealerTotal = calculateScore(dealerHand);

            while (dealerTotal < 17) {
                System.out.println("Dealer hits...");
                dealerHand.add(deck.remove(0));
                printCard(dealerHand.get(dealerHand.size() - 1));
                dealerTotal = calculateScore(dealerHand);
            }

            System.out.println("Dealer total: " + dealerTotal);

            // ---------- RESULT ----------
            if (dealerTotal > 21 || playerTotal > dealerTotal) {
                System.out.println("You win!");
                bank += wager * 2; // 1x profit
            }
            else if (playerTotal < dealerTotal) {
                System.out.println("Dealer wins.");
            }
            else {
                System.out.println("Push.");
                bank += wager; // return bet
            }

            System.out.println("Play again? (y/n)");
            String again = scan.next();
            if (!again.equalsIgnoreCase("y")) {
                playing = false;
            }
        }

        System.out.println("\nGame over. Final Bank: $" + bank);
    }

    public static void printCard(int card) {

        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] ranks = {
                "Ace", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Jack", "Queen", "King"
        };

        String suit = suits[card / 13];
        String rank = ranks[card % 13];

        int value = getBlackjackValue(card);

        System.out.println(rank + " of " + suit + " (" + value + ")");
    }

    public static int getBlackjackValue(int card) {

        int value = card % 13;

        if (value == 0) return 11;
        else if (value >= 10) return 10;
        else return value + 1;
    }

    public static int calculateScore(List<Integer> hand) {

        int total = 0;
        int aceCount = 0;

        for (int card : hand) {

            int value = card % 13;

            if (value == 0) {
                total += 11;
                aceCount++;
            }
            else if (value >= 10) {
                total += 10;
            }
            else {
                total += value + 1;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }
}
