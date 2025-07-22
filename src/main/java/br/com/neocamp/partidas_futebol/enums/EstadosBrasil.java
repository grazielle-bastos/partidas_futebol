package br.com.neocamp.partidas_futebol.enums;

/**
 * Enum que representa todas as siglas oficiais dos estados do Brasil.
 *
 * <p>
 * Utilizado para garantir que apenas siglas válidas de estados brasileiros sejam usadas no sistema,
 * evitando erros de digitação e facilitando validações.
 * </p>
 *
 * <b>Didática:</b>
 * <ul>
 *   <li>Facilita a validação de dados relacionados a estados brasileiros.</li>
 *   <li>Evita o uso de strings soltas no código, promovendo segurança e padronização.</li>
 *   <li>Recomendado para uso em entidades, DTOs e validações de entrada.</li>
 * </ul>
 *
 * <b>Exemplo de uso:</b>
 * <pre>
 *     EstadosBrasil estado = EstadosBrasil.SP;
 *     System.out.println(estado); // Saída: SP
 * </pre>
 */
public enum EstadosBrasil {
    AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO
}
