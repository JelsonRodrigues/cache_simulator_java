package main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class AccessHistory {
    private int hits;
    private int misses;

    private int compulsory_misses;
    private int conflict_misses;
    private int capacity_misses;
    
    public AccessHistory() {
        this.hits = 0;
        this.misses = 0;
        this.compulsory_misses = 0;
        this.conflict_misses = 0;
        this.capacity_misses = 0;
    }

    public void addHit(){
        this.hits += 1;
    }
    
    public void addMiss(MissType miss) {
        switch (miss) {
            case COMPULSORY:
                this.compulsory_misses += 1;
                break;
            case CONFLICT:
                this.conflict_misses += 1;
                break;
            case CAPACITY:
                this.capacity_misses += 1;
                break;
        }
        this.misses += 1;
    }
    
    public String getFormattedHistory(boolean output_format) {
        String result = "";

        // define '.' como o digito separador dos decimais
        DecimalFormatSymbols decimalDot = new DecimalFormatSymbols();
        decimalDot.setDecimalSeparator('.');

        // formata a saida como um decimal com duas casas apos a virgula e com o separador definido
        DecimalFormat df = new DecimalFormat("0.00", decimalDot);
        
        if (output_format == true) {
            result = this.getTotalAccess() + " " +
            df.format(this.calculateHitRate()) + " " +
            df.format(this.calculateMissRate()) + " " +
            df.format(this.calculateCompulsoryMissPercent()) + " " + 
            df.format(this.calculateCapacityMissPercent()) + " " + 
            df.format(this.calculateConflictMissPercent()) + " ";
        }
        else {
            result = "Total de acessos: " + this.getTotalAccess() + "\n" +
                     "Total Hits: " + this.hits + "\n" + 
                     "Total Misses: " + this.misses + "\n" + 
                     "Total Misses Compulsórios: " + this.compulsory_misses + "\n" +
                     "Total Misses Capacidade: " + this.capacity_misses + "\n" +
                     "Total Misses Conflito: " + this.conflict_misses + "\n" +
                     "Taxa de Hit: " + df.format(this.calculateHitRate() * 100.0) + "%\n" +
                     "Taxa de Misses: " + df.format(this.calculateMissRate() * 100.0) + "%\n" +
                     "Taxa Misses Compulsórios: " + df.format(this.calculateCompulsoryMissPercent() * 100.0) + "%\n" +
                     "Taxa Misses Capacidade: " + df.format(this.calculateCapacityMissPercent() * 100.0) + "%\n" +
                     "Taxa Misses Conflito: " + df.format(this.calculateConflictMissPercent() * 100.0) + "%\n";
        }

        return result;
    }

    public int getTotalAccess(){
        return this.hits + this.misses;
    }

    public double calculateHitRate(){
        int totalAcess = getTotalAccess();
        if(getTotalAccess() > 0){
            return (double)this.hits/(double)totalAcess;
        }
        return 0.0;
    }
    
    public double calculateMissRate(){
        int totalAcess = getTotalAccess();
        if(getTotalAccess() > 0){
            return (double)this.misses/(double)totalAcess;
        }
        return 0.0;
    }

    public double calculateCompulsoryMissPercent(){
        if (this.misses > 0) {
            return (double) this.compulsory_misses / (double) this.misses;
        }
        return 0.0;
    }

    public double calculateCapacityMissPercent(){
        if (this.misses > 0) {
            return (double) this.capacity_misses / (double) this.misses;
        }
        return 0.0;
    }

    public double calculateConflictMissPercent(){
        if (this.misses > 0) {
            return (double) this.conflict_misses / (double) this.misses;
        }
        return 0.0;
    }
}
