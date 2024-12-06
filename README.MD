Simple SMTP Server


Descrição
Este projeto implementa um servidor SMTP simples utilizando a classe ServerSocket em Java. O servidor é projetado para simular a troca de mensagens de e-mail entre um cliente e o servidor SMTP, seguindo as especificações básicas do protocolo SMTP.

Ele aceita conexões de clientes na porta 2525 e responde aos comandos SMTP padrão como HELO, MAIL FROM, RCPT TO, DATA, e QUIT. O servidor utiliza threads para permitir que múltiplos clientes sejam atendidos simultaneamente.


Requisitos
Porta 2525 aberta para conexões TCP.
Cliente SMTP ou ferramenta como Telnet para testar a comunicação com o servidor.
3 classes principais Main clientProcessor e Server.


Fluxo de Comunicação
O cliente se conecta ao servidor e recebe uma resposta inicial de boas-vindas (220).
O cliente envia o comando HELO para iniciar a sessão, e o servidor responde com 250.
O cliente envia o comando MAIL FROM para especificar o remetente, e o servidor responde com 250.
O cliente envia o comando RCPT TO para especificar o destinatário, e o servidor responde com 250.
O cliente envia o comando DATA para começar a enviar o corpo do e-mail, e o servidor responde com 354.
O cliente envia o corpo do e-mail e termina com um ponto (.), o que indica o fim da mensagem.
O servidor responde com 250 Message accepted for delivery.
O cliente envia o comando QUIT para encerrar a sessão, e o servidor responde com 221.


Observações:
Cada conexão é tratada por uma thread para permitir atendimento simultâneo de múltiplos clientes.
Comandos SMTP
O servidor implementa validações simples para os comandos MAIL FROM, RCPT TO e DATA, garantindo que as mensagens sejam formatadas corretamente antes de continuar com a comunicação.
O servidor responde com códigos de status SMTP padrão, como 250 para sucesso e 500 para erro de sintaxe.
Validação de E-mail
O servidor valida os endereços de e-mail usando expressões regulares simples para garantir que eles sigam o formato username@domain.com.
Conclusão
Este servidor SMTP simples foi projetado para fins educacionais e serve como uma introdução ao protocolo SMTP e ao desenvolvimento de servidores de rede em Java. Ele pode ser expandido e melhorado para suportar mais funcionalidades e um maior nível de segurança e robustez.