package main.replacement_policies;

import java.util.ArrayList;

import main.Cache;
import main.MissType;
import main.errors.NotPowerOf2;

public class Fifo extends Cache {
    private class Line {
        public boolean valid;
        public int tag;

        public Line(){
            this.valid = false;
            this.tag = 0;
        }
    }

    private class Set {
        public ArrayList<Line> positions;
        public int total_bytes_used;
        public int last_index;
        public int index_first_in;
        public boolean is_full;

        public Set(int assoc) {
            this.total_bytes_used = 0;
            this.index_first_in = 0;
            this.last_index = 0;
            this.is_full = false;
            
            this.positions = new ArrayList<>(assoc);

            for (int c = 0; c < assoc; c++) {
                positions.add(new Line());
            }
        }

        public void replace(int new_tag) {
            this.positions.get(this.index_first_in).tag = new_tag;
            this.positions.get(this.index_first_in).valid = true;

            this.index_first_in = (this.index_first_in + 1) % this.positions.size(); 
        }

        public void add(int new_tag) {
            this.positions.get(this.last_index).tag = new_tag;
            this.positions.get(this.last_index).valid = true;

            this.last_index += 1;
        }
    }

    private ArrayList<Set> memory;
    private int total_bytes_used;
    private int max_capacity;

    public Fifo(int nsets, int assoc, int bsize) throws NotPowerOf2 {
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
                return;
            }
        }

        // Miss
        // Tratamento da falta e atualização do histórico de acesso
        if (set.is_full == false) {
            // Compulsorio
            set.add(tag_of_adress);

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
            
            set.replace(tag_of_adress);
        }

    }

    private boolean is_full() {
        return max_capacity == this.total_bytes_used;
    }
}

 