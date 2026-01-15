import re
import unicodedata

# ------------------------
# Limpieza base
# ------------------------
def quitar_tildes(texto: str) -> str:
    texto = unicodedata.normalize('NFD', texto)
    return ''.join(c for c in texto if unicodedata.category(c) != 'Mn')

def limpiar_texto(texto: str) -> str:
    texto = str(texto).lower()
    texto = quitar_tildes(texto)
    texto = re.sub(r'[^a-z\s]', ' ', texto)
    texto = re.sub(r'\s+', ' ', texto).strip()
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
    return re.sub(r'\bno_me\s+(\w+)', r'no_me_\1', texto)

# ------------------------
# Refuerzos
# ------------------------
def reforzar_positivos_suaves(texto: str) -> str:
    positivos = {
        "me gusto", "me encanta", "me agrada", "me fascina",
        "me encanto", "me parecio bueno", "me parecio genial"
    }
    if any(n in texto for n in ["no_me_gusto", "no_gusto","no_me_encanta"]):
        return texto
    for p in positivos:
        if p in texto:
            texto += " POSITIVO_SUAVE"
    return texto

def reforzar_negativos_fuertes(texto: str) -> str:
    patrones = [
        "se infl", "se cierr", "no funciona", "me colgo","tardo",
        "peligro", "falta de respeto", "error", "fallo",
        "pesim", "horribl", "insoport"
    ]
    for p in patrones:
        if p in texto:
            texto += " NEGATIVO_FUERTE NEGATIVO_FUERTE NEGATIVO_FUERTE"
    return texto

def reforzar_negativos_suaves(texto: str) -> str:
    patrones = [
        "esperaba algo mejor",
        "no quede satisfecho",
        "no me convencio",
        "experiencia regular",
        "la calidad no es"
    ]
    patrones = [limpiar_texto(p) for p in patrones]
    for p in patrones:
        if p in texto:
            texto += " NEGATIVO_SUAVE NEGATIVO_SUAVE NEGATIVO_SUAVE NEGATIVO_SUAVE NEGATIVO_SUAVE"
    return texto

# =========================================
# Regla general de negación semántica
# =========================================
def negacion_valoracion_positiva(texto: str) -> bool:
    patrones = [
        r"no_me_gust",
        r"no_me_encant",
        r"no_me_pareci",
        r"no_me_convenc",
        r"no_me_satisf",
        r"no_me_agrad",
        r"no_me_funcion",
        r"no_me_gusta_para_nada"
    ]
    return any(re.search(p, texto) for p in patrones)


# =========================================
#  Reglas duras de patrones negativos
# =========================================
def reglas_negativas_duras(texto: str) -> bool:
    patrones_duros = [
        "la calidad no_es",
        "no_quede satisfecho",
        "no_quedo satisfecho",
        "experiencia fue regular",
        "experiencia regular",
        "no_me_encanta",
        "no_estuvo bien",
        "no_fue una buena experiencia"
    ]
    return any(p in texto for p in patrones_duros)

def unir_no_me(texto: str) -> str:
    # Une "no me X" → "no_me_X"
    return re.sub(r'\bno_me\s+(\w+)', r'no_me_\1', texto)

# ------------------------
# Pipeline FINAL
# ------------------------
def limpiar_y_reforzar(texto: str) -> str:
    texto = limpiar_texto(texto)
    texto = unir_negaciones(texto)
    texto = unir_no_me(texto)
    texto = reforzar_positivos_suaves(texto)
    texto = reforzar_negativos_fuertes(texto)
    texto = reforzar_negativos_suaves(texto)
    return texto

# =========================================
# Regla positiva explícita (positivos claros)
# =========================================
def regla_positiva_explicita(texto: str) -> bool:
    patrones_positivos = [
        "era lo que esperaba",
        "cumple lo esperado",
        "exactamente lo que esperaba",
        "tal como esperaba"
    ]

    # Excluir negaciones explícitas
    patrones_negacion = [
        "no_era lo que esperaba",
        "no era lo que esperaba"
    ]

    if any(n in texto for n in patrones_negacion):
        return False

    return any(p in texto for p in patrones_positivos)
