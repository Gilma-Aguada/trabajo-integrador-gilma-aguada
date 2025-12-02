# ✅ VALIDACIONES IMPLEMENTADAS EN TU PROYECTO

## 📌 Resumen de cambios

He agregado validaciones completas en frontend a tu proyecto. Aquí está lo que se hizo:

---

## 1️⃣ ARCHIVO: `clientes.js` ✅

### Nueva función: `validarCliente(cliente)`
Valida:
- **Nombre**: No vacío, 3-50 caracteres
- **Email**: Formato válido de email
- **Teléfono**: 7-15 dígitos

### Cambio en: `guardarCliente()`
Antes de enviar al servidor, ahora valida todos los campos. Si hay errores, muestra lista de problemas.

**Ejemplo de error:**
```
Errores de validación:

1. El nombre es obligatorio
2. El email no es válido
3. El teléfono debe contener entre 7 y 15 dígitos
```

---

## 2️⃣ ARCHIVO: `productos.js` ✅

### Nueva función: `validarProducto(producto)`
Valida:
- **Nombre**: No vacío, 3-100 caracteres
- **Precio**: Número positivo, máximo 1.000.000
- **Stock**: No negativo, máximo 999.999

### Cambio en: `guardarProducto()`
Ahora valida antes de enviar. Impide guardar productos con datos inválidos.

---

## 📋 Cómo funciona (FLUJO)

### ANTES (sin validaciones):
```
Usuario escribe datos ❌ → Envía al servidor → Servidor rechaza → Error
```

### AHORA (con validaciones):
```
Usuario escribe datos ✅ → Validamos en frontend → Si OK envía → Servidor procesa
                                    ↓
                            Si FALTA algo → Mostramos errores → Usuario corrige
```

---

## 🎯 Tipos de validación implementados:

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| **No Vacío** | Campo obligatorio | Nombre, Email |
| **Email** | Formato válido | usuario@ejemplo.com |
| **Teléfono** | 7-15 dígitos | 1234567890 |
| **Longitud** | Min-Max caracteres | 3-50 caracteres |
| **Número** | Solo números positivos | Precio > 0 |
| **Rango** | Valores min-max | Stock 0-999.999 |

---

## 💡 Ejemplos de validaciones

### ✅ CLIENTE VÁLIDO:
- Nombre: "Juan García"
- Email: "juan@gmail.com"
- Teléfono: "1234567890"

### ❌ CLIENTE INVÁLIDO:
- Nombre: "A" (menos de 3 caracteres)
- Email: "juan.com" (sin @)
- Teléfono: "123" (menos de 7 dígitos)

---

## 🚀 Cómo probar

1. Abre http://localhost:8080/clientes.html
2. Haz clic en "Nuevo Cliente"
3. Deja campos vacíos e intenta guardar → Verás los errores
4. Llena datos inválidos e intenta guardar → Verás validaciones
5. Llena datos válidos → Se guardará correctamente

---

## 📝 Validaciones disponibles para copiar-pegar

Si quieres agregar más validaciones, aquí hay lista de funciones:

```javascript
// Validar no vacío
function validarNoVacio(valor) {
    return valor && valor.trim() !== '';
}

// Validar email
function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// Validar teléfono
function validarTelefono(telefono) {
    return /^\d{7,15}$/.test(telefono.replace(/[- ]/g, ''));
}

// Validar número positivo
function validarNumeroPositivo(numero) {
    return !isNaN(numero) && numero > 0;
}

// Validar longitud
function validarLongitud(texto, min, max) {
    return texto.length >= min && texto.length <= max;
}
```

---

## 🎓 Próximos pasos para mejorar:

1. **Agregar Toastr.js** - Mostrar mensajes de error más bonitos
2. **Validación en tiempo real** - Validar mientras el usuario escribe
3. **Feedback visual** - Cambiar color de bordes en rojo si hay error
4. **Agregar a pedidos.js** - Validar dirección de envío, cantidad, etc.

¿Quieres que implemente alguna de estas mejoras?
