# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

# Authentication Service

Microservicio de autenticación desarrollado con Spring Boot que proporciona funcionalidades de registro, login y gestión de tokens JWT.

## 🚀 Ejecución con Docker Compose

### Prerrequisitos
- Docker Desktop instalado
- Docker Compose incluido

### Pasos para ejecutar

1. **Clonar y navegar al directorio**
```bash
cd C:\Users\juanm\IdeaProjects\crediya-platform\authentication-service
```

2. **Configurar variables de entorno (opcional)**
```bash
# Copiar y editar el archivo .env si es necesario
copy .env .env.local
# Editar .env.local con tus configuraciones específicas
```

3. **Construir y ejecutar los servicios**
```bash
# Construir las imágenes y ejecutar
docker-compose up --build

# O ejecutar en segundo plano
docker-compose up --build -d
```

4. **Verificar que los servicios estén funcionando**
```bash
# Ver el estado de los contenedores
docker-compose ps

# Ver los logs
docker-compose logs -f authentication-service
```

### 🧪 Probar la funcionalidad

#### 1. Health Check
```bash
curl http://localhost:8081/actuator/health
```

#### 2. Registro de usuario
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### 3. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 4. Acceder a endpoint protegido
```bash
# Usar el token JWT obtenido del login
curl -X GET http://localhost:8081/api/auth/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

#### 5. Refresh Token
```bash
curl -X POST http://localhost:8081/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
  }'
```

### 🔧 Comandos útiles

#### Gestión de contenedores
```bash
# Detener los servicios
docker-compose down

# Detener y eliminar volúmenes (⚠️ elimina datos de BD)
docker-compose down -v

# Reconstruir solo la aplicación
docker-compose build authentication-service

# Ejecutar solo la base de datos
docker-compose up postgres-auth redis-auth

# Ver logs específicos
docker-compose logs -f postgres-auth
docker-compose logs -f redis-auth
```

#### Acceso a contenedores
```bash
# Conectar a PostgreSQL
docker-compose exec postgres-auth psql -U auth_user -d authentication_db

# Conectar a Redis
docker-compose exec redis-auth redis-cli

# Acceder al contenedor de la app
docker-compose exec authentication-service bash
```

### 🔍 Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registro de nuevo usuario |
| POST | `/api/auth/login` | Autenticación de usuario |
| POST | `/api/auth/refresh` | Renovar token JWT |
| GET | `/api/auth/profile` | Obtener perfil del usuario |
| POST | `/api/auth/logout` | Cerrar sesión |
| GET | `/actuator/health` | Health check de la aplicación |

### 🐛 Troubleshooting

#### Puerto ocupado
```bash
# Cambiar puerto en .env
APP_PORT=8082
```

#### Problemas de conectividad de BD
```bash
# Verificar logs de PostgreSQL
docker-compose logs postgres-auth

# Reiniciar solo la BD
docker-compose restart postgres-auth
```

#### Limpiar todo y empezar de nuevo
```bash
# Detener todo
docker-compose down -v

# Limpiar imágenes de Docker
docker system prune -a

# Volver a construir
docker-compose up --build
```

### 📊 Monitoreo

#### Verificar estado de servicios
```bash
# Health checks
curl http://localhost:8081/actuator/health
curl http://localhost:8081/actuator/info

# Métricas (si están habilitadas)
curl http://localhost:8081/actuator/metrics
```

#### Logs en tiempo real
```bash
# Todos los servicios
docker-compose logs -f

# Solo la aplicación
docker-compose logs -f authentication-service
```

### 🛡️ Seguridad

Para producción, recuerda:
- Cambiar todas las contraseñas por defecto
- Usar secrets de Docker para información sensible
- Configurar HTTPS
- Implementar rate limiting
- Configurar firewall apropiado
