# 📋 GUÍA DE VALIDACIONES EN FRONTEND

## 1. VALIDACIONES EN HTML (Atributos HTML5)

```html
<!-- Validación de email -->
<input type="email" class="form-control" id="email" required>

<!-- Validación de número -->
<input type="number" class="form-control" id="stock" min="0" max="10000" required>

<!-- Validación de precio -->
<input type="number" class="form-control" id="precio" step="0.01" min="0" required>

<!-- Validación de teléfono con patrón -->
<input type="tel" class="form-control" id="telefono" pattern="[0-9]{7,15}" required>

<!-- Validación de texto con longitud -->
<input type="text" class="form-control" id="nombre" minlength="3" maxlength="50" required>
```

---

## 2. VALIDACIONES EN JAVASCRIPT (Completas)

### A. Validar Email
```javascript
function validarEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Uso
if (!validarEmail(documento.getElementById('email').value)) {
    mostrarError('Email inválido');
}
```

### B. Validar Teléfono
```javascript
function validarTelefono(telefono) {
    // Solo dígitos, entre 7 y 15 caracteres
    const telefonoLimpio = telefono.replace(/[- ]/g, '');
    return /^\d{7,15}$/.test(telefonoLimpio);
}

// Uso
if (!validarTelefono(document.getElementById('telefono').value)) {
    mostrarError('Teléfono debe tener 7-15 dígitos');
}
```

### C. Validar Campo Vacío
```javascript
function validarNoVacio(valor, nombreCampo) {
    if (!valor || valor.trim() === '') {
        return `${nombreCampo} es obligatorio`;
    }
    return null;
}

// Uso
const error = validarNoVacio(document.getElementById('nombre').value, 'Nombre');
if (error) mostrarError(error);
```

### D. Validar Número Positivo
```javascript
function validarNumeroPositivo(numero, nombreCampo) {
    const num = parseFloat(numero);
    if (isNaN(num) || num <= 0) {
        return `${nombreCampo} debe ser un número positivo`;
    }
    return null;
}

// Uso
const error = validarNumeroPositivo(document.getElementById('precio').value, 'Precio');
if (error) mostrarError(error);
```

### E. Validar Longitud de Texto
```javascript
function validarLongitud(texto, minimo, maximo, nombreCampo) {
    const longitud = texto.trim().length;
    if (longitud < minimo || longitud > maximo) {
        return `${nombreCampo} debe tener entre ${minimo} y ${maximo} caracteres`;
    }
    return null;
}

// Uso
const error = validarLongitud(document.getElementById('nombre').value, 3, 50, 'Nombre');
if (error) mostrarError(error);
```

---

## 3. EJEMPLO COMPLETO: VALIDAR CLIENTE

```javascript
// Función que valida TODO un cliente
function validarCliente(cliente) {
    const errores = [];
    
    // 1. Validar nombre
    if (!cliente.nombre || cliente.nombre.trim() === '') {
        errores.push('El nombre es obligatorio');
    } else if (cliente.nombre.trim().length < 3) {
        errores.push('El nombre debe tener al menos 3 caracteres');
    } else if (cliente.nombre.trim().length > 50) {
        errores.push('El nombre no puede exceder 50 caracteres');
    }
    
    // 2. Validar email
    if (!cliente.email || cliente.email.trim() === '') {
        errores.push('El email es obligatorio');
    } else {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(cliente.email)) {
            errores.push('El email no es válido (ej: usuario@ejemplo.com)');
        }
    }
    
    // 3. Validar teléfono
    if (!cliente.telefono || cliente.telefono.trim() === '') {
        errores.push('El teléfono es obligatorio');
    } else {
        const telefonoLimpio = cliente.telefono.replace(/[- ]/g, '');
        if (!/^\d{7,15}$/.test(telefonoLimpio)) {
            errores.push('El teléfono debe contener entre 7 y 15 dígitos');
        }
    }
    
    return errores;
}

// Función para guardar CON VALIDACIONES
async function guardarCliente() {
    const id = document.getElementById('clienteId').value;
    const cliente = {
        nombre: document.getElementById('nombre').value.trim(),
        email: document.getElementById('email').value.trim(),
        telefono: document.getElementById('telefono').value.trim()
    };
    
    // VALIDAR PRIMERO
    const errores = validarCliente(cliente);
    if (errores.length > 0) {
        const mensaje = errores.map((err, i) => `${i + 1}. ${err}`).join('\n');
        alert('Errores de validación:\n\n' + mensaje);
        return; // DETENER LA EJECUCIÓN
    }
    
    // Si pasó validaciones, enviar al servidor
    try {
        const url = id ? `${API_URL}/${id}` : API_URL;
        const metodo = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cliente)
        });
        
        if (!response.ok) throw new Error('Error al guardar');
        
        alert('Guardado correctamente');
        modalInstance.hide();
        cargarClientes();
    } catch (error) {
        alert('Error: ' + error.message);
    }
}
```

---

## 4. VALIDACIÓN EN TIEMPO REAL (Mientras escribe)

```javascript
// Validar email mientras escribe
document.getElementById('email').addEventListener('blur', function() {
    const email = this.value;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (email && !emailRegex.test(email)) {
        this.classList.add('is-invalid');
        this.classList.remove('is-valid');
    } else if (email) {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Validar nombre mientras escribe
document.getElementById('nombre').addEventListener('input', function() {
    if (this.value.trim().length >= 3) {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    } else if (this.value.length > 0) {
        this.classList.add('is-invalid');
        this.classList.remove('is-valid');
    }
});
```

---

## 5. EJEMPLO PARA PRODUCTOS

```javascript
function validarProducto(producto) {
    const errores = [];
    
    // Validar nombre
    if (!producto.nombre || producto.nombre.trim() === '') {
        errores.push('El nombre del producto es obligatorio');
    } else if (producto.nombre.length < 3) {
        errores.push('El nombre debe tener al menos 3 caracteres');
    }
    
    // Validar precio
    const precio = parseFloat(producto.precio);
    if (isNaN(precio) || precio <= 0) {
        errores.push('El precio debe ser un número positivo');
    }
    
    // Validar stock
    const stock = parseInt(producto.stock);
    if (isNaN(stock) || stock < 0) {
        errores.push('El stock no puede ser negativo');
    }
    
    return errores;
}
```

---

## 6. USAR EN HTML (Feedback Visual)

```html
<div class="mb-3">
    <label for="email" class="form-label">Email *</label>
    <input type="email" class="form-control" id="email" required>
    <div class="invalid-feedback" id="emailError">
        Por favor, ingrese un email válido
    </div>
</div>

<style>
.form-control.is-valid {
    border-color: #198754;
    background-image: url("data:image/svg+xml,.."); /* checkmark */
}

.form-control.is-invalid {
    border-color: #dc3545;
    background-image: url("data:image/svg+xml,.."); /* X mark */
}
</style>
```

---

## 7. RESUMEN QUICK (Lo más importante)

| Validación | Código |
|-----------|--------|
| Campo no vacío | `if (!valor \|\| valor.trim() === '')` |
| Email | `/^[^\s@]+@[^\s@]+\.[^\s@]+$/` |
| Teléfono | `/^\d{7,15}$/` |
| Número positivo | `valor > 0 && !isNaN(valor)` |
| Longitud | `valor.length >= min && valor.length <= max` |
| Fecha válida | `new Date(valor)` |

---

**Tip:** Siempre valida ANTES de enviar al servidor para mejor experiencia de usuario.
