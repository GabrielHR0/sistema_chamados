<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Chamados - Login</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <!-- AdminLTE CSS -->
    <link href="<c:url value='/adminlte/dist/css/adminlte.min.css' />" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <style>
        * {
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
        }
        html,
        body {
            height: 100%;
        }
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .login-page {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            width: 100%;
        }
        .login-box {
            width: 360px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
            overflow: hidden;
        }
        .login-logo {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px 20px;
            text-align: center;
            font-weight: 600;
            font-size: 24px;
            letter-spacing: 1px;
        }
        .login-logo i {
            margin-right: 10px;
        }
        .login-body {
            padding: 30px 25px;
        }
        .login-body p {
            color: #666;
            font-size: 14px;
            margin-bottom: 25px;
            text-align: center;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            font-size: 13px;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
        }
        .form-group input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 13px;
            transition: all 0.3s;
        }
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        .alert {
            font-size: 13px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-danger {
            background-color: #fee;
            border: 1px solid #fcc;
            color: #c33;
        }
        .alert-success {
            background-color: #efe;
            border: 1px solid #cfc;
            color: #3c3;
        }
        .btn-login {
            width: 100%;
            padding: 10px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 4px;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 10px;
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            color: white;
            text-decoration: none;
        }
        .btn-login:active {
            transform: translateY(0);
        }
        .login-footer {
            padding: 15px 25px;
            text-align: center;
            border-top: 1px solid #eee;
            background: #f9f9f9;
            font-size: 12px;
            color: #999;
        }
        .login-footer i {
            margin-right: 5px;
        }
        .remember-me {
            font-size: 13px;
            margin-top: -10px;
            margin-bottom: 15px;
        }
        .remember-me input[type="checkbox"] {
            margin-right: 5px;
            cursor: pointer;
        }
        .remember-me label {
            margin: 0;
            cursor: pointer;
            font-weight: normal;
            display: flex;
            align-items: center;
        }
        @media (max-width: 480px) {
            .login-box {
                width: 90%;
                max-width: 360px;
            }
            .login-body {
                padding: 20px 15px;
            }
            .login-logo {
                padding: 20px 15px;
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="login-page">
        <div class="login-box">
            <div class="login-logo">
                <i class="fas fa-ticket-alt"></i>
                Chamados
            </div>
            <div class="login-body">
                <p>Acesse sua conta</p>
                
                <c:if test="${hasError}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle"></i>
                        ${error}
                    </div>
                </c:if>
                
                <c:if test="${hasLogout}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i>
                        ${logout}
                    </div>
                </c:if>
                
                <form method="post" action="${pageContext.request.contextPath}/perform_login">
                    <div class="form-group">
                        <label for="username">Usuário</label>
                        <input 
                            type="text" 
                            id="username" 
                            name="username" 
                            class="form-control" 
                            placeholder="Seu e-mail ou usuário"
                            required
                            autocomplete="username">
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Senha</label>
                        <input 
                            type="password" 
                            id="password" 
                            name="password" 
                            class="form-control" 
                            placeholder="Sua senha"
                            required
                            autocomplete="current-password">
                    </div>
                    
                    <div class="remember-me">
                        <label>
                            <input type="checkbox" name="remember-me" value="on">
                            Lembrar-me neste dispositivo
                        </label>
                    </div>
                    
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    
                    <button type="submit" class="btn btn-login">
                        <i class="fas fa-sign-in-alt"></i>
                        Entrar
                    </button>
                </form>
            </div>
            
            <div class="login-footer">
                <i class="fas fa-shield-alt"></i>
                Conexão segura
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Disable submit button while processing
        document.querySelector('form').addEventListener('submit', function() {
            const btn = document.querySelector('.btn-login');
            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Entrando...';
        });
        
        // Focus on first input
        document.querySelector('#username').focus();
    </script>
</body>
</html>
