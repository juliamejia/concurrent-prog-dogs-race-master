Escuela Colombiana de Ingeniería

Arquitecturas de Software – ARSW  
#### Integrantes : Cristian Rodriguez y Julia Mejia 

####Taller – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.

#####Parte I – Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.  
    <img width="381" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/258dd18d-c733-4788-9523-2d0973cc4819">  

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.  
    <img width="384" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/ad685cc1-1287-4894-a325-ee0b28c024eb">  

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.  
* modificado en la clase Main   
  <img width="343" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/d4027bcf-1d3f-4865-bbeb-b80c349856e5">  
  <img width="260" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/302e7676-995a-472b-9d35-3fea621ce3fe">  

#####Parte II 

Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Taller.

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:  

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.  
    * Antes:  
      <img width="632" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/5276d11f-c5e3-4776-b11a-557cd5d98d6b">
        
    * Corregido en la clase MainCanodromo  
      <img width="401" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/7ea3e3a1-4c2a-4282-8b9d-b16b92c311e8">  
2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.
    * Varios Galgos llegan en la misma posición  
      <img width="258" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/325c84e1-94b5-47ba-972e-7a1e72b687c3">  
      <img width="448" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/c3feda39-c689-4596-901a-25bfef536197">  
    * En varias oportunidades no muestra correctamente el total de galgos  
      <img width="198" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/5c4420f5-5dee-47c9-90f4-b076495ed7d8">
      
3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.
    * Se Sincronizó en la clase Galgo donde se obtiene la posicion alcanzada , con el siguiente codigo  
      `if (paso == carril.size()) {`  
	  `    carril.finish(); // Marca el carril como terminado`  
	  `    int ubicacion;`  
	  `    synchronized (regl) {`  
	  `        ubicacion = regl.getUltimaPosicionAlcanzada();`  
	  `        regl.setUltimaPosicionAlcanzada(ubicacion + 1);`  
	  `        System.out.println("El galgo " + this.getName() + " llegó en la posicion " + ubicacion);`  
	  `    }`    
	  `    if (ubicacion == 1) {`  
	  `        regl.setGanador(this.getName()); // Establece al ganador si llega primero`  
	  `    }`  
	  `}`    

      #### Resultados  
        <img width="639" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/055a08f8-66e5-4971-a06a-5b86012dacdd">  
        
         Como podemos ver las posiciones salen en orden  
        <img width="344" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/892780be-47b0-41d7-ba82-963609bfe5a2">  


4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).
    * En la clase MainCanodromo se agrega la accion de los botones Stop y Continue
    *  En la clase Galgo se implementan los siguientes metodos  
      ` public void dormirse() {`  
	  `     parar = true; // Pone al galgo en estado de pausa`  
	  ` }`  
	  ` public void despertarse() {`  
	  ` 	synchronized (this) {`  
	  ` 		parar = false; // Reanuda al galgo si estaba en pausa`   
	  ` 		notifyAll(); // Notifica a todos los hilos que esperaban por 'wait'`   
	  ` 	}`   
	  ` }`  
    #### Resultados    
    * Pausar Carrera  
      <img width="628" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/6af44bd0-9c48-464f-bbd6-ed9542f6ae88">  
      <img width="168" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/9c96bb47-cf66-434c-8a6a-109cb4957071">  
    * Renaudar Carrera
      
      <img width="624" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/b4958eeb-3e18-4be1-b2d8-cae46551c474">  
      
      <img width="275" alt="image" src="https://github.com/juliamejia/concurrent-prog-dogs-race-master/assets/98657146/323ba01d-debf-48e6-8b48-ead94e3c633c">  



      

      

      


## Criterios de evaluación

1. Funcionalidad.

    1.1. La ejecución de los galgos puede ser detenida y resumida consistentemente.
    
    1.2. No hay inconsistencias en el orden de llegada registrado.
    
2. Diseño.   

    2.1. Se hace una sincronización de sólo la región crítica (sincronizar, por ejemplo, todo un método, bloquearía más de lo necesario).
    
    2.2. Los galgos, cuando están suspendidos, son reactivados son sólo un llamado (usando un monitor común).

