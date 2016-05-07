package clasesGeneticoNeuronal2;

import clasesGeneticoNeuronal2.*;
import interfacesAlgoritmoGenetico.Seleccion;

public class SeleccionRN implements Seleccion{
	double elite;

	public SeleccionRN(double elite) {
		this.elite=elite;
	}
	@Override
	public Poblacion seleccionar(Poblacion p1, Poblacion p2) {
		
		
		Poblacion resultado = new Poblacion(p1.nIndvs, p1.capas);;
		double elites = p1.getIndividuos().size()*elite;
		for(int i=0; i<elites; i++){
			Individuo maxInv = null;
			double max =-1;
			for (Individuo inv: p1.getIndividuos()){
				if(inv.getFit()>max){
					max=inv.getFit();
					maxInv=inv;
				}
			}
			resultado.individuos.add(maxInv);
			p1.individuos.remove(maxInv);
		}
		
		for(int i=0; i<p2.getIndividuos().size()-elites; i++){
			resultado.individuos.add(p2.individuos.get(i));
		}
		return resultado;
	}
}
