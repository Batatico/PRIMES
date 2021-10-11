package cl.ucn.disc.hpc.primes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
main class

 */
@Slf4j
public class main {

    /**
     * verifica que es primo
     *
     * @param n the number to test
     * @return true is n is prime.
     */
    public static boolean isPrime(final long n){

        for(int i = 2; i * i <= n; i++){
            if( n % i == 0 ){
                return false;
            }
        }

        return true;
    }

    /**
     *  MAIN
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        int cores = 6;

        log.debug("INICIANDO TAREA CON {} NUCLEOS", cores);

        //cronometro
        final StopWatch watch =  StopWatch.createStarted();
        long start = System.currentTimeMillis();

        //Creacion de paquetes de hilos
        final ExecutorService executorService = Executors.newFixedThreadPool(cores);


        for(long x = 2; x <= 1000000; x++){
            executorService.submit(new Tarea(x));
        }
        executorService.shutdown();

        executorService.awaitTermination(1, TimeUnit.SECONDS);

        long time = System.currentTimeMillis() - start;

        log.debug("se encuentran {} primos en {}ms usando {} nucleos",Tarea.getCont(),time,cores);

    }

    private static class Tarea implements Runnable {

        private long numero ;
        private static final AtomicInteger cont = new AtomicInteger(0);

        Tarea(long numero){
            this.numero = numero;
        }

        public static int getCont() {
            return cont.get();
        }

        @Override
        public void run() {
            if(isPrime(this.numero)){
                cont.incrementAndGet();
            }
        }
    }
}
