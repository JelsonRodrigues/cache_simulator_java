package main.replacement_policies;

import java.util.ArrayList;

import main.Cache;
import main.MissType;
import main.errors.NotPowerOf2;

public class Mru extends Cache {
    private class Line {
        public boolean valid;
        public int tag;

        public Line(int age_bits){
            this.valid = false;
            this.tag = 0;
        }
    }

    private class Set {
        public ArrayList<Line> positions;
        public int index_most_recently_used;
        public int last_index;
        public boolean is_full;

        public Set(int assoc) {
            this.index_most_recently_used = 0;
            this.last_index = 0;
            this.is_full = false;
            
            this.positions = new ArrayList<>(assoc);

            for (int c = 0; c < assoc; c++) {
                positions.add(new Line(c));
            }
        }

        private void replace(int new_tag) {
            int position = this.index_most_recently_used;
            this.positions.get(position).tag = new_tag;
            this.positions.get(position).valid = true;
        }

        private void add(int new_tag) {
            this.positions.get(this.last_index).tag = new_tag;
            this.positions.get(this.last_index).valid = true;
            
            this.index_most_recently_used = last_index;

            this.last_index += 1;
            if (this.last_index >= this.positions.size()) {
                this.is_full = true;
            }

            
        }

        public void write(int new_tag) {
            if (this.is_full) {
                replace(new_tag);
            }
            else {
                add(new_tag);
            }
        }

        public void read(int index) {
            this.index_most_recently_used = index;
        }

    }

    private ArrayList<Set> memory;
    private int total_bytes_used;
    private int max_capacity;

    public Mru(int nsets, int assoc, int bsize) throws NotPowerOf2 {
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
                set.read(c);
                return;
            }
        }

        // Miss
        // Tratamento da falta e atualização do histórico de acesso
        if (set.is_full == false) {
            // Compulsorio 
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

        set.write(tag_of_adress);
    }

    private boolean is_full() {
        return max_capacity == this.total_bytes_used;
    }
}

 