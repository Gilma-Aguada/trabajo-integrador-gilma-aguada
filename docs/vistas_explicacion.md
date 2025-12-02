
┌───────────────────────────────┐
│        Backend Spring Boot    │
│  (Controller, Service, Repo)  │
└───────────────────────────────┘
                │
   ┌────────────┴─────────────┐
   │                          │
   ▼                          ▼
┌─────────────────────┐   ┌────────────────┐
│     VISTA THYMELEAF │   │VISTA JAVASCRIPT│
│(motor de plantillas)│ (frontend dinámico)│
└───────────┬─────────┘   └──────────┬─────┘
            │                        │
            │ Renderiza contenido    │ Interacción directa
            │ preparado por servidor │ con el usuario
            │ (datos ya procesados)  │ (formularios, botones, CRUD)
            ▼                        ▼
┌──────────────────┐        ┌─────────────────────┐
│Ejemplo: Dashboard│        │ Ejemplo: Página de  │
│ -Total clientes  │        │ clientes            │
│ -Total pedidos   │        │ - Agregar cliente   │
│ - Bajo stock     │        │ - Editar cliente    │
│                  │        │ - Eliminar cliente  │
└──────────────────┘        └─────────────────────┘
