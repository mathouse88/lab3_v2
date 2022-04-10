/* LAB4
 Mateusz Sliwa, 10.04.2022

    Problem producentów i konsumentów z usypianiem wątków
    + proszę uzupełnić rozwiązanie problemu (jak w Lab 3) dla 1 producenta i 1 konsumenta z użyciem tradycyjnych mechanizmów Javy wait() i notify()
    o pomiar czasu używając metody System.nanoTime() oraz wstawić do kodu wywołanie metody sleep()
    + wykonać program z róznymi wartościami czasu, na który wątek zostanie uśpiony
    + porównać wydajność oraz przebieg wykonania programu dla implementacji własnej opartej o wait() / notify(),
     a implementacji z użyciem jednego z mechanizmów JCU (czas wykonania oraz sekwencję operacji wykonywanych przez producenta i konsumenta).

*/

class Producent extends Thread {
    private final Bufor _buf;

    public Producent(Bufor b) { _buf = b; }     // inicjalizacja bufora

    public void run() {
        for (int i = 1; i < 101; i++) {         // petla od 0 do 100
            try { sleep(50); }
            catch (Exception e) { System.out.println(e); }
            _buf.put(i);                        // utworzenie elementu i , start od 0
        }
    }
}

class Konsument extends Thread {
    private final Bufor _buf;

    public Konsument(Bufor b) { _buf = b; }     // inicjalizacja bufora

    public void run() {
        for (int i = 1; i < 101; i++) {         // petla od 0 do 100
            try { sleep(50); }
            catch (Exception e) { System.out.println(e); }
            _buf.get();                         // pobranie elementu i, start od 0
        }
    }
}

class Bufor {
    private int zawartoscBufora;
    private boolean czyDostepny = false;

    public synchronized void put(int wartosc) {
        while (czyDostepny) {                           // warunek oczekiwania
            try { wait(); }
            catch (Exception e) { System.out.println(e); }
        }
        System.out.println("Producent " + (wartosc));   // wyswietl aktualna wartosc
        zawartoscBufora = wartosc;                      // podstaw wartosc pod zawartoscBufora
        czyDostepny = true;                             // zmien wartosc dostepnosci bufora na true
        notify();                                       // obudz uspiony watek
    }

    public synchronized void get() {
        while (!czyDostepny) {                          // warunek oczekiwania
            try { wait(); }
            catch (Exception e) { System.out.println(e); }
        }
        System.out.println("Konsument " + (zawartoscBufora) + "\n");    // wyswietl zawartosc bufora
        czyDostepny = false;                            // zmien wartosc dostepnosci bufora na false
        notify();                                       // obudz uspiony watek
    }
}

public class lab3_v2 {
    public static void main(String[] args) {

        // utworzenie obiektow
        Bufor b = new Bufor();
        Producent p = new Producent(b);
        Konsument k = new Konsument(b);

        // start watkow
        p.start();
        k.start();
    }
}