package edu.eci.arsw.primefinder;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class Main {
	public static void main(String[] args) throws InterruptedException {
		LinkedList<PrimeFinderThread> primeFinderThreads= new LinkedList<>();
		PrimeFinderThread pft1=new PrimeFinderThread(0, 10000000);
		PrimeFinderThread pft2=new PrimeFinderThread(10000001, 20000000);
		PrimeFinderThread pft3=new PrimeFinderThread(20000001, 30000000);
		primeFinderThreads.add(pft1);
		primeFinderThreads.add(pft2);
		primeFinderThreads.add(pft3);


		//corre los threads
		for(PrimeFinderThread thread:primeFinderThreads){
			thread.start();
		}
		//Empezamos a contar los 5 segundos
		long startTime = System.nanoTime();
		TimeUnit.SECONDS.sleep(5);
		long endTime = System.nanoTime();
		long timeElapse = endTime-startTime;

		//Duerme los hilos
		for(PrimeFinderThread thread:primeFinderThreads){
			thread.suspender();
		}
		//crea una lista con los numeros primos e imprime la cantidad
		List<Integer> primos = PrimeFinderThread.getPrimes();
		System.out.println("Cantidad de primos: " +primos.size());

		//pide enter como entrada
		Scanner scanner = new Scanner(System.in);
		System.out.println("Presione enter para continuar");
		String input = scanner.nextLine();

		//Despierta los hilos
		if(Objects.equals(input, "")){
			for(PrimeFinderThread thread:primeFinderThreads){
				thread.reanudar();
			}
		}
	}
}