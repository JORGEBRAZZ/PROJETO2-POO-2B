import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

class Endereco implements Serializable {                                                                                //COMPOSIÇÃO
    String rua;
    String cidade;
    String estado;

    public Endereco(String rua, String cidade, String estado) {
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void mostrarEndereco() {
        System.out.println("Endereço: " + rua + ", " + cidade + " - " + estado);
    }
}

class Prato implements Serializable { // Classe representando um prato de cada restaurante // A classe pode ser convertida em uma sequência de bytes.
    String nome;
    double preco;
    String descricao;

    public Prato(String nome, double preco, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }

    public void mostrar() {
        System.out.println(nome + " - R$ " + preco + ": " + descricao);
    }
}

class PratoSalgado extends Prato {
    public PratoSalgado(String nome, double preco, String descricao) {
        super(nome, preco, descricao);
    }

    @Override
    public void mostrar() {
        System.out.println("[Prato Salgado] " + nome + " - R$ " + preco + ": " + descricao);
    }
}

class PratoDoce extends Prato {
    public PratoDoce(String nome, double preco, String descricao) {
        super(nome, preco, descricao);
    }

    @Override
    public void mostrar() {
        System.out.println("[Prato Doce] " + nome + " - R$ " + preco + ": " + descricao);
    }
}
                                                                                                                         //Acoplamento
class Restaurante implements Serializable {
    String nome;
    String cnpj;
    Prato prato1;
    Prato prato2;
    Prato prato3;

    public Restaurante(String nome, String cnpj, Prato p1, Prato p2, Prato p3) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.prato1 = p1;
        this.prato2 = p2;
        this.prato3 = p3;
    }

    public void mostrarCardapio() {
        System.out.println("Restaurante: " + nome);
        System.out.println("CNPJ: " + cnpj);
        System.out.println("Cardápio: ");
        prato1.mostrar();
        prato2.mostrar();
        prato3.mostrar();
        System.out.println();
    }
}

class Cliente implements Serializable {                                                                         //Coesão de cliente
    String nome;
    String CPF;
    int idCliente;
    Endereco endereco;                                                                                                       

    public Cliente(String nome, String CPF, int idCliente, Endereco endereco) {
        this.nome = nome;
        this.CPF = CPF;
        this.idCliente = idCliente;
        this.endereco = endereco;
    }

    public void mostrarCliente() {
        System.out.println("Cliente " + idCliente);
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + CPF);
        endereco.mostrarEndereco();                                                                         //Composição sendo utilizado
        System.out.println();
    }
}

                                                                                                             // Coesão de Pedido
class Pedido implements Serializable {
    Prato pratoPedido1;
    Prato pratoPedido2;
    Restaurante restaurante;
    Cliente cliente;
    String observacao;
    private static int totalPedidos = 0;
    private int idPedido;

    public Pedido(Cliente cliente, Prato p1, Prato p2, Restaurante r) {
        this.cliente = cliente;
        this.pratoPedido1 = p1;
        this.pratoPedido2 = p2;
        this.restaurante = r;
        this.observacao = "Sem observações!";
        totalPedidos++;
        idPedido = totalPedidos;
    }

    public Pedido(Cliente cliente, Prato p1, Prato p2, Restaurante r, String ob) {
        this.cliente = cliente;
        this.pratoPedido1 = p1;
        this.pratoPedido2 = p2;
        this.restaurante = r;
        this.observacao = ob;
        totalPedidos++;
        idPedido = totalPedidos;
    }

    public static int getTotalPedidosCriados() {
        return totalPedidos;
    }

    public void mostrarPedido() {
        System.out.println("Id do Pedido: " + idPedido);
        System.out.println("Pedido do cliente: " + cliente.nome);
        System.out.println("Restaurante escolhido: " + restaurante.nome);
        System.out.println("Pratos escolhidos: " + pratoPedido1.nome + " e " + pratoPedido2.nome);
        System.out.println("Observação: " + observacao);
        System.out.printf("Total do pedido: R$ %.2f\n", calcularTotal());
        System.out.println();
    }

    public double calcularTotal() {
        return pratoPedido1.preco + pratoPedido2.preco;
    }

    @Override
    public String toString() {
        return "Pedido {" + cliente.nome + ": " + pratoPedido1.nome + ", " + pratoPedido2.nome + ", Restaurante: " + restaurante.nome + " OBS: " + observacao + "}";
    }
}

//Serialização e persistência

class Carrinho {
    public static void salvarPedidos(ArrayList<Pedido> pedidos, String arquivo) { // método para salvar os pedidos em um arquivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(pedidos); // Serializa a lista de pedidos
            System.out.println("Pedidos salvos!"); //Menssagem que confirma o salvamento dos pedidos
        } catch (IOException e) { //Captura possíveis erros no carregamento dos arquivos
            System.out.println("Não foi possível salvar seus pedidos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Pedido> carregarPedidos(String arquivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (ArrayList<Pedido>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar pedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

class CarrinhoGUI {
    public static void exibirCarrinho(Pedido pedido) { //Mostra a interface para um pedido por vez
        JFrame frame = new JFrame("Carrinho de " + pedido.cliente.nome);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        //informações sobre o pedido
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText("Carrinho:\n");
        area.append("Cliente: " + pedido.cliente.nome + "\n");
        area.append("Restaurante: " + pedido.restaurante.nome + "\n");
        area.append("Itens:\n");
        area.append("- " + pedido.pratoPedido1.nome + "\n");
        area.append("- " + pedido.pratoPedido2.nome + "\n");
        area.append("Total: R$ " + String.format("%.2f", pedido.calcularTotal()) + "\n");

        JButton btnConfirmar = new JButton("Confirmar Pedido");
        btnConfirmar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Pedido Confirmado!");

            // Mostra apenas esse pedido confirmado
            JFrame janela = new JFrame("Pedido Confirmado");
            janela.setSize(400, 300);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel painel = new JPanel();
            painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

            painel.add(new JLabel("  Cliente: " + pedido.cliente.nome));
            painel.add(new JLabel("  Restaurante: " + pedido.restaurante.nome));
            painel.add(new JLabel("  Pratos: " + pedido.pratoPedido1.nome + " e " + pedido.pratoPedido2.nome));
            painel.add(new JLabel("  Observação: " + pedido.observacao));
            painel.add(new JLabel(String.format("  Total: R$ %.2f", pedido.calcularTotal())));
            painel.add(new JLabel(" ---------------------------------------------------------------------"));

            JButton btOK = new JButton("OK!");
            btOK.addActionListener(i -> janela.dispose());
            painel.add(Box.createVerticalStrut(10));
            painel.add(btOK);

            janela.add(painel);
            janela.setVisible(true);

            frame.dispose(); // Fecha a janela do carrinho
        });

        frame.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
        frame.getContentPane().add(btnConfirmar, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}


public class Main {
    public static void main(String[] args) {

        Prato p1 = new PratoSalgado("Pizza (G) de Calabresa", 40.0, "Molho, Queijo Mussarela, Calabresa e orégano");
        Prato p2 = new PratoSalgado("Pizza (G) de Frango C/ Catupiry", 42.0, "Molho, Queijo Mussarela, Frango e Catupiry");
        Prato p3 = new PratoDoce("Pizza (P) de Chocolate", 25.0, "Queijo Mussarela e chocolate ao leite.");

        Prato p4 = new PratoSalgado("Carne na Brasa (700g)", 35.0, "Acompanha Arroz, Farofa e Vinagrete");
        Prato p5 = new PratoSalgado("Frango na Brasa (700g)", 30.0, "Acompanha Arroz, Farofa e Vinagrete");
        Prato p6 = new PratoSalgado("Mistão (Serve 3 pessoas)", 60.0, "Carne, Frango e Linguiça acompanhados de Arroz, Farofa, Vinagrete e Pirão de queijo");

        Prato p7 = new PratoSalgado("Natal 084", 22.0, "Hambúrguer artesanal, Queijo Mussarela, bacon, cebola caramelizada, salada e molho especial");
        Prato p8 = new PratoSalgado("SGA 084", 20.0, "Frango Cremoso, Queijo Mussarela, Salada, Batata Palha e molho especial");
        Prato p9 = new PratoSalgado("Macaíba 084", 25.0, "Hambúrguer artesanal, Queijo Cheddar, Farofa de Bacon e Geleia de Pimenta");

        Restaurante r1 = new Restaurante("Pizzaria 084", "13.267.588/0001-05", p1, p2, p3);
        Restaurante r2 = new Restaurante("Churrascaria 084", "15.190.340/0001-33", p4, p5, p6);
        Restaurante r3 = new Restaurante("Hamburgueria 084", "18.333.406/0001-12", p7, p8, p9);

                                                                                                                 //Endereços Composição
        Endereco e1 = new Endereco("Rua das Flores, 100", "Natal", "RN");
        Endereco e2 = new Endereco("Av. Central, 200", "Parnamirim", "RN");
        Endereco e3 = new Endereco("Rua Projetada, 300", "Macaíba", "RN");

        Cliente c1 = new Cliente("Pedro", "123.456.789-10", 1, e1);
        Cliente c2 = new Cliente("Leonardo", "109.876.543-21", 2, e2);
        Cliente c3 = new Cliente("Daniel", "321.654.987-01", 3, e3);

        Pedido pedido1 = new Pedido(c1, p2, p3, r1, "Sem queijo mussarela na pizza de chocolate");
        Pedido pedido2 = new Pedido(c2, p5, p6, r2);
        Pedido pedido3 = new Pedido(c3, p8, p9, r3, "Sem alfaces na salada");

        r1.mostrarCardapio();
        r2.mostrarCardapio();
        r3.mostrarCardapio();

        c1.mostrarCliente();
        c2.mostrarCliente();
        c3.mostrarCliente();

        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        listaPedidos.add(pedido1);
        listaPedidos.add(pedido2);
        listaPedidos.add(pedido3);

        listarPedidosPorCliente("Pedro", listaPedidos);
        listarPedidosPorCliente("Leonardo", listaPedidos);
        listarPedidosPorCliente("Daniel", listaPedidos);

        listarPedidosPorRestaurante("Pizzaria 084", listaPedidos);
        listarPedidosPorRestaurante("Churrascaria 084", listaPedidos);
        listarPedidosPorRestaurante("Hamburgueria 084", listaPedidos);

        try {
            Restaurante[] restaurantes = { r1, r2, r3 };
            System.out.println(restaurantes[3].nome);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro: tentou acessar um restaurante que não existe.");
        }

        System.out.println("Total de Pedidos: " + Pedido.getTotalPedidosCriados());
        System.out.println("Descrição: " + pedido1.toString());
        System.out.println("Descrição: " + pedido2.toString());
        System.out.println("Descrição: " + pedido3.toString());
        System.out.println();

        // Carrinho
        ArrayList<Pedido> restaurados = Carrinho.carregarPedidos("pedidos.dat"); // restaura os pedidos salvos
        System.out.println("Pedidos restaurados:");
        for (Pedido p : restaurados) { // Lê os pedidos restaurados e exibe as informações no terminal.
        p.mostrarPedido();
        }
        Carrinho.salvarPedidos(listaPedidos, "pedidos.dat"); // Atualiza o arquivo.

        //INTERFACE
      for (Pedido pedido : listaPedidos) {
    CarrinhoGUI.exibirCarrinho(pedido);
	}

         Map<String, String> contatosClientes = new TreeMap<>();
        contatosClientes.put("Cliente 01", "(84) 99658-1793");
        contatosClientes.put("Cliente 02", "(84) 96809-5572");
        contatosClientes.put("Cliente 03", "(84) 98616-4221");

        System.out.println();
        System.out.println("Telefone para contato - Pedro: " + contatosClientes.get("Cliente 01"));
        System.out.println("Telefone para contato - Leonardo: " + contatosClientes.get("Cliente 02"));
        System.out.println("Telefone para contato - Daniel: " + contatosClientes.get("Cliente 03"));
        System.out.println();

        Set<Integer> codigo = new TreeSet<>();
        codigo.add(1025);
        codigo.add(1026);
        codigo.add(1027);

        System.out.println("Códigos para confirmação de entrega por cliente respectivo: " + codigo);
        System.out.println();
    }


    public static void listarPedidosPorCliente(String nomeCliente, ArrayList<Pedido> pedidos) {
        for (Pedido p : pedidos) {
            if (p.cliente.nome.equalsIgnoreCase(nomeCliente)) {
                p.mostrarPedido();
            }
        }
    }

    public static void listarPedidosPorRestaurante(String nomeRestaurante, ArrayList<Pedido> pedidos) {
        System.out.println("Pedidos do restaurante: " + nomeRestaurante);
        for (Pedido p : pedidos) {
            if (p.restaurante.nome.equalsIgnoreCase(nomeRestaurante)) {
                p.mostrarPedido();
            }
        }
    }
}


