import {
  setupWorker,
  type MockedRequest,
  type RestHandler,
  type DefaultBodyType,
} from 'msw';
import { setupServer } from 'msw/node';

const handlers: RestHandler<MockedRequest<DefaultBodyType>>[] = [];

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
