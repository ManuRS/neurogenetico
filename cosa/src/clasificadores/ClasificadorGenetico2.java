package clasificadores;

import java.io.*;
import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import clasesGeneticoNeuronal2.*;
import datos.Datos;
import interfacesAlgoritmoGenetico.*;

public class ClasificadorGenetico2 extends Clasificador {

	EndAlgGen parada;
	Seleccion selecc;
	Fitness fit;
	IniPoblacion inip;
	Mutacion mut;
	Recombinar reco;
	SelectPadres sepad;
	Individuo seleccionado;
	ArrayList<Double> res;
	int tpob, ngen;
	
	List<Integer> capas;
	int nIndvs;
	
	public ClasificadorGenetico2(EndAlgGen parada, Seleccion selecc, Fitness fit, IniPoblacion inip,
			Mutacion mut, Recombinar reco, SelectPadres sepad, int tpob, int ngen) {
		this.parada= parada;
		this.selecc= selecc;
		this.fit=fit;
		this.inip= inip;
		this.mut=mut;
		this.reco= reco;
		this.sepad= sepad;
		this.res= new ArrayList<Double>();
		this.tpob= tpob;
		this.ngen= ngen;
	}
	
	
	@Override
	public void entrenamiento(Datos datosTrain) {
		
		FileWriter out;
		try {
			out = new FileWriter("fits/geneticoNums"+tpob+"-"+ngen+".data");
		} catch (IOException e1) {
			out=null;
			e1.printStackTrace();
			return;
		}
		System.out.println("EMPIEZA");
		Poblacion p = inip.crearPoblacion();
		Poblacion p2=null;
		System.out.println(p.getIndividuos().get(0).getRed().getRed().getNeuronas().get(0).getPesos());
		
		fit.fit(p, datosTrain);
		
		double mejor=p.getIndividuos().get(0).getFit();
		double peor=Double.MAX_VALUE;
		Double fitact;
		
		while(!parada.end(p.getIndividuos().get(0).getFit())){

			p2=sepad.selectPadres(p);
			p2= reco.recombinar(p2);
			
			mut.mutar(p2);
			fit.fit(p2, datosTrain);
			p=selecc.seleccionar(p, p2);

			fitact=p.fitMejor();
			try {
				out.write(p.fitMejor()+" "+p.fitMedio()+" "+p.fitPeor()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fitact>mejor){
				mejor=fitact;
				System.out.println("fit mejor:"+p.fitMejor()+" fit medio:"+p.fitMedio()
				+" fit peor:"+p.fitPeor());
			}
			
				/*peor= Double.MAX_VALUE;
			for(Individuo i : p.getIndividuos()){
				if (peor> i.getFit())
					peor= i.getFit();
			}
			try {
				out.write(mejor+"\t"+p.sumFit()/p.getIndividuos().size()+"\t"+peor+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		double max = -1;
		for (Individuo inv: p.getIndividuos()){
			if(inv.getFit()>max){
				max=inv.getFit();
				seleccionado=inv;
			}
		}
		//seleccionado.getRed().entrenamiento(datosTrain);
		System.out.println("\nFin train elegido:"+seleccionado.getFit()+"\n");
		System.out.println("A continuación se muestran los alelos activos para cada gen de individuo seleccionado");
		
		System.out.println();
		/*try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Double> clasifica(Datos datosTest) {
		return clasifica3(datosTest);

	}
	
	private ArrayList<Double> clasifica3(Datos datosTest){
		
		Individuo i= seleccionado;
		res=i.getRed().clasifica(datosTest);
		
		
		return res;
	}
	
	/*private ArrayList<Double> clasifica1(Datos datosTest) {
		fit.generarBitSets(datosTest);
		int numPriori = 0;
		
		for(int i=0; i<datosTest.getNumDatos(); i++){
			boolean cond2=false;
			for(Regla r: seleccionado.getReglas()){
				boolean cond=true;
			    for(int j=0; j<datosTest.getSizeTipoAtributos()-1; j++){
					BitSet num1 = datosTest.getDatoBS(i, j);
					BitSet num2 = r.getRegla().get(j);
					Boolean b = num1.intersects(num2);
					if(b==false){
						cond=false;
						break;
					}
			    }
			    if(cond==true){
		    		Enumeration<Double> keys= datosTest.getHashClase().keys();
		    		while(keys.hasMoreElements()){
		    			Double key=keys.nextElement();
		    			if (datosTest.getHashClase().get(key).equals(r.getPrediccion())==true){
		    				res.add(key);
		    				cond2=true;
		    				break;
		    			}
		    		}
		    		break;
		    	}
			}
			if(cond2==false){
	    		res.add(apriori);
	    		numPriori++;
			}
		}
		System.out.println("Datos predecidos por priori   = "+numPriori);
		System.out.println("Datos predecidos por genetico = "+(datosTest.getNumDatos()-numPriori)+"\n");
		return res;
	}
	
	private ArrayList<Double> clasifica2(Datos datosTest) {
		fit.generarBitSets(datosTest);
		int numPriori = 0;
		int s1=0;
		int s2=0;
		
		for(int i=0; i<datosTest.getNumDatos(); i++){
			boolean cond2=false;
			
			for(Regla r: seleccionado.getReglas()){
				boolean cond=true;
			    for(int j=0; j<datosTest.getSizeTipoAtributos()-1; j++){
					BitSet num1 = datosTest.getDatoBS(i, j);
					BitSet num2 = r.getRegla().get(j);
					Boolean b = num1.intersects(num2);
					if(b==false){
						cond=false;
						break;
					}
			    }
			    if(cond==true){
		    		Enumeration<Double> keys= datosTest.getHashClase().keys();
		    		while(keys.hasMoreElements()){
		    			Double key=keys.nextElement();
		    			System.out.println("key="+key);
		    			if (datosTest.getHashClase().get(key).equals(r.getPrediccion())==true){
		    				if(key==2.0)
		    					s1++;
		    				if(key==4.0)
		    					s2++;
		    				cond2=true;
		    				break;
		    			}
		    		}
		    	}
			}
			if(cond2==false){
	    		res.add(apriori);
	    		numPriori++;
			}
			else{
				Enumeration<Double> keys= datosTest.getHashClase().keys();
				Double key=keys.nextElement();
				System.out.println("S1="+s1+" // S2="+s2);
				if(s1>s2){
					res.add(key);
				}else if (s2>s1){
					key=keys.nextElement();
					res.add(key);
				}
				else{
		    		res.add(apriori);
		    		numPriori++;
				}
				s1=0;
				s2=0;
			}
		}
		System.out.println("Datos predecidos por priori   = "+numPriori);
		System.out.println("Datos predecidos por genetico = "+(datosTest.getNumDatos()-numPriori)+"\n");
		return res;
	}*/

	@Override
	public ArrayList<Double> getResultado() {
		return res;
	}
}
