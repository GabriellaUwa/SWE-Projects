package edu.qc.seclass.tipcalculator;

public class Calculate {

    public int costPerPerson;

    public void calculate(int amount, int partySize){

        costPerPerson = Math.round(amount/partySize);
    }

    public int getFifteen(){

        return (int)Math.ceil((costPerPerson * 0.15));
    }

    public int getTwenty(){

        return (int)Math.ceil((costPerPerson * 0.20));
    }

    public int getTwentyfive(){

        return (int)Math.ceil((costPerPerson * 0.25));
    }

    public int getTotalFifteen(){

        return costPerPerson + getFifteen();
    }

    public int getTotalTwenty(){

        return costPerPerson + getTwenty();
    }

    public int getTotalTwentyFive(){

        return costPerPerson + getTwentyfive();
    }

}
