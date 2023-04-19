import {
  setupWorker,
  type MockedRequest,
  type RestHandler,
  type DefaultBodyType,
} from 'msw';
import { setupServer } from 'msw/node';
import { serverHandlers } from './handlers/server';

const handlers: RestHandler<MockedRequest<DefaultBodyType>>[] = [
  ...serverHandlers,
];

function initMocks() {
  if (typeof window === 'undefined') {
    const server = setupServer(...handlers);
    server.listen();
  } else {
    const worker = setupWorker(...handlers);
    worker.start();
  }
}

initMocks();

export {};
