import re
import unicodedata

# =====================================================
# 1️⃣ LIMPIEZA BASE
# =====================================================

def quitar_tildes(texto: str) -> str:
    texto = unicodedata.normalize('NFD', texto)
    return ''.join(c for c in texto if unicodedata.category(c) != 'Mn')

def limpiar_texto(texto: str) -> str:
    texto = str(texto).lower()
    texto = quitar_tildes(texto)
    texto = re.sub(r'[^a-z\s]', ' ', texto)
    texto = re.sub(r'\s+', ' ', texto).strip()
    return texto

def unir_frases_largas(texto: str) -> str:
    frases = [
        "no lo recomiendo",
        "no supero mis expectativas",
        "no cumple con mis expectativas",
        "no estoy conforme",
        "era lo que esperaba",
        "no era lo que esperaba",
        "el servicio fue correcto",
        "diferente a lo que muestran",
        "si me gusto",
        "precio es excesivo",
        "exactamente lo que buscaba",
        "muy satisfecho",
        "muy insatisfecho",
        "volveria sin dudarlo",
        "no volveria sin dudarlo",
        "diseno es agradable",
        "diseño es agradable",
        "muy ergonomico",
        "no volveria a comprar",
        "valio la pena",
        "no valio la pena",
        "cumplio mis expectativas",
        "cumple con mis expectativas",
        "supero mis expectativas",
        "supera mis expectativas",
        "me convencio del todo",
        "no tiene errores",
        "no tiene fallos"
    ]
    for f in frases:
        texto = texto.replace(f, f.replace(" ", "_"))
    return texto

def unir_negaciones(texto: str) -> str:
    negaciones = {'no', 'ni', 'nunca', 'jamas', 'sin'}
    palabras = texto.split()
    resultado = []
    i = 0
    while i < len(palabras):
        if palabras[i] in negaciones and i + 1 < len(palabras):
            resultado.append(f"{palabras[i]}_{palabras[i+1]}")
            i += 2
        else:
            resultado.append(palabras[i])
            i += 1
    return ' '.join(resultado)

def unir_no_me(texto: str) -> str:
    texto = re.sub(r'\bno_me\s+(\w+)', r'no_me_\1', texto)
    texto = re.sub(r'\bno_me_(encanta|gusta|convencio)', r'no_me_\1', texto)
    return texto

# =====================================================
# 2️⃣ REFUERZOS
# =====================================================

def reforzar_positivos_suaves(texto: str) -> str:
    positivos = [
        "me gusta", "me encanto", "me encanta",
        "buen servicio", "muy bueno",
        "fue buena", "es buena",
        "experiencia fue buena",
        "producto aceptable", "aceptable"
    ]
    for p in positivos:
        if p in texto:
            texto += " POSITIVO_BAJO"
    return texto

def reforzar_negativos_fuertes(texto: str) -> str:
    patrones = [
        r"muy lenta", r"errores", r"falla", r"fallo",
        r"peligro", r"horrible", r"pesimo", r"pesima",
        r"malisima",
        r"tardaron mucho", r"llego fria",
        r"enganosa", r"engañoso", r"publicacion enganosa",
        r"llego danado", r"producto danado",
        r"nadie respondio", r"no respondieron",
        r"calidad\s*(es\s*)?muy baja",
        r"muy baja para el precio",
        r"baja calidad",
        r"insatisfech"
    ]
    for p in patrones:
        if re.search(p, texto):
            texto += " NEGATIVO_FUERTE NEGATIVO_FUERTE NEGATIVO_FUERTE"
    return texto

def reforzar_negativos_suaves(texto: str) -> str:
    suaves = [
        "esperaba algo mejor",
        "no quede satisfecho",
        "no me convencio",
        "experiencia regular",
        "la calidad no es",
        "demasiado costoso",
        "me arrepiento de haberlo comprado"
    ]
    for s in suaves:
        if s in texto:
            texto += " NEGATIVO_SUAVE NEGATIVO_SUAVE"
    return texto

# =====================================================
# 3️⃣ REGLAS DURAS
# =====================================================

def reglas_negativas_duras(texto: str) -> bool:
    return any(p in texto for p in [
        "no_quede_satisfecho",
        "muy_insatisfecho",
        "no_me_encanta",
        "no_estuvo_bien",
        "no_fue_una_buena_experiencia",
        "no_fue_buena_experiencia",
        "experiencia_regular",
        "no_volveria_a_comprar",
        "no_volveria_sin_dudarlo",
        "no_era_lo_que_esperaba",
        "no_lo_recomiendo",
        "no_valio_la_pena",
        "no_cumplio_mis_expectativas",
        "no_supero_mis_expectativas",
        "mala_experiencia",
        "perdida_de_tiempo",
        "no_estoy_conforme"
    ])

def negacion_valoracion_positiva(texto: str) -> bool:
    return any(re.search(p, texto) for p in [
        r"no_me_gust",
        r"no_me_encant",
        r"no_me_convenc",
        r"no_funciona"
    ])

def regla_positiva_explicita(texto: str) -> bool:
    return any(p in texto for p in [
        "era_lo_que_esperaba",
        "exactamente_lo_que_buscaba",
        "muy_satisfecho",
        "me_encanto",
        "me_encanta",
        "funciona_de_maravilla",
        "muy_ergonomico",
        "diseno_es_agradable",
        "volveria_sin_dudarlo",
        "valio_la_pena"
    ])

def regla_expectativas_positivas(texto: str) -> bool:
    return any(p in texto for p in [
        "cumplio_mis_expectativas",
        "cumple_con_mis_expectativas",
        "supero_mis_expectativas",
        "supera_mis_expectativas",
        "me_convencio_del_todo"
    ])

def regla_gusto_explicito(texto: str) -> bool:
    return any(p in texto for p in [
        "si_me_gusto",
        "me_gusto_la",
        "me_gusto_mucho"
    ])

def regla_neutro_positivo(texto: str) -> bool:
    return "servicio_fue_correcto" in texto

def contraste_con_negativo(texto: str) -> bool:
    conectores = ["pero", "aunque", "sin embargo"]
    negativos = [
        "precio_es_excesivo",
        "muy_caro",
        "precio_elevado",
        "fallo",
        "errores",
        "no_volveria_a_comprar",
        "calidad_es_muy_baja"
    ]
    for c in conectores:
        if c in texto:
            despues = texto.split(c, 1)[1]
            if any(n in despues for n in negativos):
                return True
    return False

def regla_conformidad_positiva(texto: str) -> bool:
    positivos = ["conforme", "satisfech"]
    if any(p in texto for p in positivos):
        if not any(n in texto for n in ["no_", "nunca_", "jamas_"]):
            return True
    return False

# =====================================================
# 🟡 REGLA GENERAL PARA EXPECTATIVAS, ERRORES Y SATISFACCIÓN
# =====================================================
def regla_expectativas_generales(texto: str) -> str:
    # NEGATIVOS ABSOLUTOS
    if any(p in texto for p in [
        "no_cumple_con_mis_expectativas",
        "no_supero_mis_expectativas",
        "no_valio_la_pena",
        "insatisfech"
    ]):
        return "NEGATIVO"

    # POSITIVOS ABSOLUTOS
    if any(p in texto for p in [
        "cumplio_mis_expectativas",
        "cumple_con_mis_expectativas",
        "supero_mis_expectativas",
        "supera_mis_expectativas",
        "no_tiene_errores",
        "no_tiene_fallos",
        "valio_la_pena",
        "muy_satisfecho"
    ]):
        return "POSITIVO"

    return ""


# =====================================================
# 4️⃣ PIPELINE FINAL
# =====================================================

def limpiar_y_reforzar(texto: str) -> str:
    texto = limpiar_texto(texto)
    texto = unir_frases_largas(texto)
    texto = unir_negaciones(texto)
    texto = unir_no_me(texto)
    texto = reforzar_positivos_suaves(texto)
    texto = reforzar_negativos_fuertes(texto)
    texto = reforzar_negativos_suaves(texto)
    return texto