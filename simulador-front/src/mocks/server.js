// server.js
import { setupServer } from 'msw/node';
import { handlers } from './handlers';

// Configura el servidor con los handlers
const server = setupServer(...handlers);

// Inicia el servidor antes de las pruebas
beforeAll(() => server.listen());

// Restablece los handlers entre pruebas
afterEach(() => server.resetHandlers());

// Detiene el servidor después de las pruebas
afterAll(() => server.close());
