package main.politicas_substituicao;

import java.util.ArrayList;

import main.Cache;
import main.MissType;
import main.errors.NotPowerOf2;

public class Lru extends Cache {
    /*
     * A lógica utilizada para implementação do LRU foi https://youtu.be/bq6N7Ym81iI
    */
    private class Line {
        public boolean valid;
        public int tag;
        public int age_bits;

        public Line(int age_bits){
            this.valid = false;
            this.tag = 0;
            this.age_bits = age_bits;
        }
    }

    private class Set {
        public ArrayList<Line> positions;
        public int total_bytes_used;
        public int index_least_recently_used;
        public boolean is_full;

        public Set(int assoc) {
            this.total_bytes_used = 0;
            this.index_least_recently_used = 0;
            this.is_full = false;
            
            this.positions = new ArrayList<>(assoc);

            for (int c = 0; c < assoc; c++) {
                positions.add(new Line(c));
            }
        }

        public void updateAgeBits(int most_recently_used_index) {
            int age_bits_acceced_value = this.positions.get(most_recently_used_index).age_bits;
            for (int c = 0; c < this.positions.size(); c++) {
                Line line = this.positions.get(c);
                if (line.age_bits > age_bits_acceced_value) {
                    line.age_bits -= 1;
                }
                if (line.age_bits == 0) {
                    index_least_recently_used = c;
                }
            }
            this.positions.get(most_recently_used_index).age_bits = this.positions.size() - 1;
        }
    }

    private ArrayList<Set> memory;
    private int total_bytes_used;
    private int max_capacity;

    public Lru(int nsets, int assoc, int bsize) throws NotPowerOf2 {
        super(nsets, assoc, bsize);
        this.total_bytes_used = 0;
        this.max_capacity = nsets * assoc * bsize;
        this.memory = new ArrayList<>(nsets);
        for (int c = 0; c < nsets; c++){
            this.memory.add(new Set(assoc));
        }
    }

    @Override
    public void read(int address) {
        int index_of_adress = this.getIndex(address);
        int tag_of_adress = this.getTag(address);

        Set set = memory.get(index_of_adress);

        int assoc = this.getAssoc();

        // Procura o valor na cache
        for (int c = 0; c < assoc; c++) {
            Line line = set.positions.get(c);
            if (line.valid && line.tag == tag_of_adress) {
                this.getHistory().addHit();
                set.updateAgeBits(c);
                return;
            }
        }

        // Miss
        // Tratamento da falta e atualização do histórico de acesso
        if (set.is_full == false) {
            // Compulsorio
            set.total_bytes_used += getBsize();
            if (set.total_bytes_used >= this.getAssoc() * this.getBsize()) {
                set.is_full = true;
            }

            this.total_bytes_used += this.getBsize();

            this.getHistory().addMiss(MissType.COMPULSORY);
        }
        else {
            if (this.is_full()) {
                // Capacidade
                this.getHistory().addMiss(MissType.CAPACITY);
            }
            else {
                // Conflito
                this.getHistory().addMiss(MissType.CONFLICT);
            }
        }

        int position = set.index_least_recently_used;
        Line line = set.positions.get(position); 
        line.tag = tag_of_adress;
        line.valid = true;
        set.updateAgeBits(position);
    }

    private boolean is_full() {
        return max_capacity == this.total_bytes_used;
    }
}

 