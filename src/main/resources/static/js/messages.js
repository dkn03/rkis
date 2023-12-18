// Функция для отправки сообщения на ActiveMQ
function sendMessage(id) {

    // Создание объекта Stomp
    var client = Stomp.client('ws://localhost:61614');

    // Подключение к брокеру
    client.connect({}, function () {
        // Отправка сообщения в очередь (замените 'your-queue' на имя вашей очереди)
        client.send('messagesQueue', {}, "Куплен товар c id = " + id);
    });
}