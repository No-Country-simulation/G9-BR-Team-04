import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Trend } from 'k6/metrics';

// Métricas customizadas para acompanhar o comportamento do Circuit Breaker
const errosCircuitBreaker = new Counter('erros_circuit_breaker_503');
const latenciaClassificacao = new Trend('latencia_classificacao');

// Textos de exemplo variados, pra não bater sempre no cache SHA-256
// (senão você só testaria a velocidade do cache, não do fluxo real de ML)
const textos = [
  'Spring Boot é um framework Java para construção de aplicações robustas',
  'Machine Learning é uma área da inteligência artificial focada em padrões',
  'Docker permite empacotar aplicações em containers isolados e portáveis',
  'Kubernetes orquestra containers em ambientes distribuídos de produção',
  'Oracle Database 23ai traz suporte nativo a busca vetorial e IA generativa',
  'React é uma biblioteca JavaScript para construção de interfaces de usuário',
  'Circuit Breaker é um padrão de resiliência para evitar falhas em cascata',
  'Virtual Threads no Java 21 permitem alta concorrência com baixo overhead',
  'PostgreSQL é um banco de dados relacional open source muito utilizado',
  'REST APIs seguem princípios de arquitetura para comunicação entre sistemas',
];

export const options = {
  scenarios: {
    // Cenário 1: carga normal e constante, simulando uso real
    carga_normal: {
      executor: 'constant-vus',
      vus: 10,
      duration: '30s',
      startTime: '0s',
      tags: { cenario: 'normal' },
    },
    // Cenário 2: pico repentino de usuários, pra estressar o sistema
    // e observar o Circuit Breaker/Bulkhead reagindo
    pico_de_carga: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 100 }, // sobe rápido até 100 VUs
        { duration: '20s', target: 100 }, // mantém o pico
        { duration: '10s', target: 0 },   // desce
      ],
      startTime: '35s', // começa depois que o cenário normal termina
      tags: { cenario: 'pico' },
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<2000'], // 95% das requisições abaixo de 2s
    http_req_failed: ['rate<0.5'],     // menos de 50% de falha (ajuste conforme esperado)
  },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  const texto = textos[Math.floor(Math.random() * textos.length)];

  const payload = JSON.stringify({
    titulo: `Conteúdo de teste - VU ${__VU} - iter ${__ITER}`,
    texto: texto,
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
    timeout: '15s',
  };

  const res = http.post(`${BASE_URL}/conteudo`, payload, params);

  latenciaClassificacao.add(res.timings.duration);

  const ok = check(res, {
    'status é 200 ou 201': (r) => r.status === 200 || r.status === 201,
  });

  if (res.status === 503) {
    errosCircuitBreaker.add(1);
  }

  if (!ok) {
    console.log(`Falha: status=${res.status} body=${res.body}`);
  }

  sleep(1); // pausa entre requisições de cada VU, simula usuário real pensando
}