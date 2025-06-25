using MailKit.Net.Smtp;           
using MailKit.Security;
using MimeKit;
using MimeKit.Text;
using Microsoft.Extensions.Options;

namespace BE.Email;

public interface IEmailService
{
    Task SendAsync(string to, string subject, string htmlContent);
    Task SendAsync(string from, string to, string subject, string htmlContent);
}

public sealed class EmailService : IEmailService
{
    private readonly EmailSetting _cfg;

    public EmailService(IOptions<EmailSetting> opts)
    {
        _cfg = opts.Value;
    }

    public async Task SendAsync(string to, string subject, string htmlContent)
    {
        await SendAsync(_cfg.From, to, subject, htmlContent);
    }

    public async Task SendAsync(string from, string to, string subject, string htmlContent)
    {
        var msg = new MimeMessage();
        msg.From.Add(MailboxAddress.Parse(from));
        msg.To.Add(MailboxAddress.Parse(to));
        msg.Subject = subject;

        msg.Body = new BodyBuilder {
            HtmlBody = htmlContent
        }.ToMessageBody();

        using var smtp = new SmtpClient();
        await smtp.ConnectAsync(_cfg.SmtpServer, _cfg.Port, SecureSocketOptions.StartTls);
        await smtp.AuthenticateAsync(_cfg.Username, _cfg.Password);
        await smtp.SendAsync(msg);
        await smtp.DisconnectAsync(true);
    }
}
