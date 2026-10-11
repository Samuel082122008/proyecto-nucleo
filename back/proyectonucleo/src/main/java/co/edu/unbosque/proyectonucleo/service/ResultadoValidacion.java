package co.edu.unbosque.proyectonucleo.service;

import java.util.ArrayList;
import java.util.List;

public class ResultadoValidacion {

	private final List<String> errores = new ArrayList<>();
	private final List<String> advertencias = new ArrayList<>();

	public void error(String m) { errores.add(m); }
	public void advertencia(String m) { advertencias.add(m); }

	public void unir(ResultadoValidacion o) {
		errores.addAll(o.errores);
		advertencias.addAll(o.advertencias);
	}

	public boolean esValido() { return errores.isEmpty(); }
	public List<String> getErrores() { return errores; }
	public List<String> getAdvertencias() { return advertencias; }
}