from flask import Flask, request, jsonify
import joblib

from nlp_utils import (
    limpiar_y_reforzar,
    reglas_negativas_duras,
    negacion_valoracion_positiva,
    regla_positiva_explicita,
    regla_expectativas_positivas,   
    regla_gusto_explicito,
    regla_neutro_positivo,
    contraste_con_negativo,
    regla_conformidad_positiva,
    regla_expectativas_generales
)

app = Flask(__name__)

# ============================================================
# 1️⃣ CARGA DE MODELOS
# ============================================================
try:
    vectorizador = joblib.load("model/vectorizador.joblib")
    modelo = joblib.load("model/modelo.joblib")
    print("✅ Modelo y vectorizador cargados con éxito.")
except Exception as e:
    modelo = None
    vectorizador = None
    print(f"X Error al cargar modelo/vectorizador: {e}")

# ============================================================
# 2️⃣ FUNCIÓN DE PREDICCIÓN (API)
# ============================================================
def predecir_sentimiento_api(texto: str, umbral: float = 0.60):
    texto_limpio = limpiar_y_reforzar(texto)
    palabras = texto_limpio.split()

    #  NEGACIONES ABSOLUTAS DE EXPECTATIVAS (PRIORIDAD MÁXIMA)
    absolutos_negativos = [
        "no_cumple_con_mis_expectativas",
        "no_supero_mis_expectativas",
        "no_valio_la_pena",
        "no_volveria_a_comprar",
        "no_lo_recomiendo",
        "no_era_lo_que_esperaba",
        "muy_insatisfecho",
        "mala_experiencia"
    ]
    for neg in absolutos_negativos:
        if neg in texto_limpio:
            return "NEGATIVO", 1.0

    # NEGATIVOS FUERTES GENERALES
    if reglas_negativas_duras(texto_limpio):
        return "NEGATIVO", 1.0

    if negacion_valoracion_positiva(texto_limpio):
        return "NEGATIVO", 0.85

    if contraste_con_negativo(texto_limpio):
        return "NEGATIVO", 0.75

    #  REGLA GENERAL DE EXPECTATIVAS / ERRORES
    resultado_expectativas = regla_expectativas_generales(texto_limpio)
    if resultado_expectativas == "POSITIVO":
        return "POSITIVO", 0.80
    elif resultado_expectativas == "NEGATIVO":
        return "NEGATIVO", 0.85

    #  POSITIVOS EXPLÍCITOS
    if regla_conformidad_positiva(texto_limpio):
        return "POSITIVO", 0.80

    if regla_expectativas_positivas(texto_limpio):
        return "POSITIVO", 0.90

    if regla_positiva_explicita(texto_limpio):
        return "POSITIVO", 1.0

    if regla_gusto_explicito(texto_limpio):
        return "POSITIVO", 0.95

    if regla_neutro_positivo(texto_limpio):
        return "POSITIVO", 0.60

    # ********* MODELO ML********************
    if modelo is None or vectorizador is None:
        return "NEUTRO", 0.50

    X = vectorizador.transform([texto_limpio])
    prob = modelo.predict_proba(X)[0]  # [neg, pos]

    #  REFUERZOS NEGATIVOS
    if "NEGATIVO_FUERTE" in palabras:
        prob[0] = min(prob[0] + 0.35, 1.0)
        prob[1] = 1 - prob[0]

    if "NEGATIVO_SUAVE" in palabras:
        prob[0] = min(prob[0] + 0.20, 1.0)
        prob[1] = 1 - prob[0]

    #  POSITIVO BAJO
    if (
        "POSITIVO_BAJO" in palabras
        and "NEGATIVO_FUERTE" not in palabras
        and "NEGATIVO_SUAVE" not in palabras
    ):
        prob[1] = min(prob[1] + 0.12, 1.0)
        prob[0] = 1 - prob[1]

    #  POSITIVO SUAVE
    if "POSITIVO_SUAVE" in palabras and "NEGATIVO_SUAVE" not in palabras:
        prob[1] = min(prob[1] + 0.10, 1.0)
        prob[0] = 1 - prob[1]

    # ✅ DECISIÓN FINAL
    if prob[1] >= umbral:
        return "POSITIVO", round(prob[1], 2)
    else:
        return "NEGATIVO", round(prob[0], 2)


# ============================================================
# 3️⃣ ENDPOINT
# ============================================================
@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()
    if not data or "texto" not in data:
        return jsonify({"error": "JSON inválido"}), 400

    texto = data["texto"]
    sentimiento, probabilidad = predecir_sentimiento_api(texto)

    return jsonify({
        "texto": texto,
        "prevision": sentimiento,
        "probabilidad": probabilidad
    })

# ============================================================
# 4️⃣ MAIN
# ============================================================
if __name__ == "__main__":
    print("🚀 API NLP lista – puerto 8000")
    app.run(host="0.0.0.0", port=8000)