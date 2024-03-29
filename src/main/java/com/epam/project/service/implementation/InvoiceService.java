package com.epam.project.service.implementation;

import com.epam.project.dao.*;
import com.epam.project.domain.*;
import com.epam.project.exceptions.*;
import com.epam.project.service.Button;
import com.epam.project.service.IInvoiceServ;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvoiceService implements IInvoiceServ {

    private static final Logger log = Logger.getLogger(ProductService.class);
    private static final DataBaseSelector source = DataBaseSelector.MY_SQL;
    private static DaoFactory daoFactory;
    private static IInvoiceDao invoiceDao;
    private static IPaymentDao paymentDao;
    private static IProductDao productDao;
    private static ITransactionDao transactionDao;
    private static TransactionService transactionService;
    private static ProductService productService;

    static {
        try {
            daoFactory = DaoFactory.getDaoFactory(source);
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            transactionDao = daoFactory.getTransactionDao();
            transactionService = new TransactionService();
            productService = new ProductService();
        } catch (IncorrectPropertyException | DataBaseConnectionException | DataBaseNotSupportedException ex) {
            log.error(ex);
        }
    }

    /** CRUD methods */

    @Button
    @Override
    public List<Invoice> findAllInvoices() {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findAllInvoices();
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public List<Invoice> findInvoices(Integer from, Integer offset) {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findInvoices(from, offset);
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public List<Invoice> findNewInvoices() {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findAllNewInvoices();
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public List<Invoice> findFinishedInvoices() {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findAllFinishedInvoices();
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public List<Invoice> findCancelledInvoices() {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findAllCancelledInvoices();
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public List<Invoice> findInvoicesByUser(String userName) {
        List<Invoice> invoices = new LinkedList<>();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoices = invoiceDao.findAllInvoicesByUser(userName);
            for (Invoice invoice : invoices) {
                if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                    daoFactory.rollbackTransaction();
            }
            daoFactory.commitTransaction();
            invoices.forEach((invoice) -> invoice.setCost(this.calculateTotalCost(invoice)));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoices;
    }

    @Button
    @Override
    public Invoice findInvoiceByOrderNumber(Long orderNum) {
        Invoice invoice = new Invoice();
        try {
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            invoice = invoiceDao.findInvoiceByOrderNumber(orderNum);
            if (!addPaymentsToInvoice(invoice, paymentDao, productDao))
                daoFactory.rollbackTransaction();
            daoFactory.commitTransaction();
            invoice.setCost(this.calculateTotalCost(invoice));
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
        }
        return invoice;
    }

    @Button
    @Override
    public synchronized boolean addInvoice(Invoice invoice) {
        try {
            if (!validateInvoice(invoice))
                return false;
            Set<String> productCodes = invoice.getPayments().keySet();
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            if (!invoiceDao.addInvoiceToDB(invoice)) {
                daoFactory.rollbackTransaction();
                return false;
            }
            for (String productCode : productCodes) {
                Product product = productDao.findProductByCode(productCode);
                Payment payment = invoice.getPayments().get(productCode);
                if ((product.getQuantity() - payment.getQuantity()) < 0 ) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
                product.setQuantity(product.getQuantity() - payment.getQuantity());
                product.setReservedQuantity(product.getReservedQuantity() + payment.getQuantity());
                if ((!paymentDao.addPaymentToDB(invoice.getPayments().get(productCode)))
                    || (!productDao.updateProductInDB(product))) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
            }
            daoFactory.commitTransaction();
        } catch (DataNotFoundException | DataBaseConnectionException ex) {
            return false;
        }
        return true;
    }

    @Button
    @Override
    public synchronized boolean updateInvoice(Invoice invoice) {
        if (!validateInvoice(invoice))
            return false;
        Invoice oldInvoice = findInvoiceByOrderNumber(invoice.getInvoiceCode());
        if (!deleteInvoice(oldInvoice))
            return false;
        if (!addInvoice(invoice)) {
            addInvoice(oldInvoice);
            return false;
        }
        return true;
    }

    @Button
    @Override
    public boolean deleteInvoice(Long orderCode) {
        Invoice invoice = findInvoiceByOrderNumber(orderCode);
        return !((invoice == null) || (invoice.getStatus() != InvoiceStatus.CREATED) || (invoice.getPaid()))
                && deleteInvoice(invoice);
    }

    private synchronized boolean deleteInvoice(Invoice invoice) {
        try {
            Set<String> productCodes = invoice.getPayments().keySet();
            daoFactory.beginTransaction();
            invoiceDao = daoFactory.getInvoiceDao();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            for (String productCode : productCodes) {
                Product product = productDao.findProductByCode(productCode);
                Payment payment = invoice.getPayments().get(productCode);
                product.setQuantity(product.getQuantity() + payment.getQuantity());
                product.setReservedQuantity(product.getReservedQuantity() - payment.getQuantity());
                if ((!paymentDao.deletePaymentFromDB(payment)) || (!productDao.updateProductInDB(product))) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
            }
            if (!invoiceDao.deleteInvoiceFromDB(invoice)) {
                daoFactory.rollbackTransaction();
                return false;
            }
            daoFactory.commitTransaction();
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            return false;
        }
        return true;
    }

    @Button
    @Override
    public boolean cancelInvoice(Long orderCode) {
        Invoice invoice = findInvoiceByOrderNumber(orderCode);
        return !((invoice == null) || (invoice.getStatus() != InvoiceStatus.CREATED)) && cancelInvoice(invoice);
    }

    private synchronized boolean cancelInvoice(Invoice invoice) {
        Set<String> productCodes = invoice.getPayments().keySet();
        try {
            daoFactory.beginTransaction();
            productDao = daoFactory.getProductDao();
            paymentDao = daoFactory.getPaymentDao();
            invoiceDao = daoFactory.getInvoiceDao();
            transactionDao = daoFactory.getTransactionDao();
            for (String productCode : productCodes) {
                Product product = productDao.findProductByCode(productCode);
                Payment payment = paymentDao.findPaymentById(invoice.getPayments().get(productCode).getPaymentId());
                product.setReservedQuantity(product.getReservedQuantity() - payment.getQuantity());
                product.setQuantity(product.getQuantity() + payment.getQuantity());
                payment.setStatusId(InvoiceStatus.CANCELLED);
                if (invoice.getPaid()) {
                    Transaction refund = transactionService.createTransactionFromPayment(payment, invoice.getUserName(), TransactionType.REFUND);
                    if (!transactionDao.addTransactionToDB(refund)) {
                        daoFactory.rollbackTransaction();
                        return false;
                    }
                }
                if ((!productDao.updateProductInDB(product)) || (!paymentDao.updatePaymentInDB(payment))) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
            }
            invoice.setStatus(InvoiceStatus.CANCELLED);
            if (!invoiceDao.updateInvoiceInDB(invoice)) {
                daoFactory.rollbackTransaction();
                return false;
            }
            daoFactory.commitTransaction();
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
            return false;
        }
        return true;
    }

    /** Special methods */

    @Button
    @Override
    public boolean removeProductFromInvoice(Long orderCode, String productCode) {
        Invoice invoice = findInvoiceByOrderNumber(orderCode);
        return !((invoice == null) || (invoice.getStatus() != InvoiceStatus.CREATED))
                && removeProductFromInvoice(invoice, productCode);
    }

    private synchronized boolean removeProductFromInvoice(Invoice invoice, String productCode) {
        if (invoice.getProducts().containsKey(productCode)
                && invoice.getPayments().containsKey(productCode)) {
            Payment payment = invoice.getPayments().get(productCode);
            invoice.getPayments().remove(productCode);
            invoice.getProducts().remove(productCode);
            return removePayment(payment);
        }
        return false;
    }

    @Button
    @Override
    public boolean confirmPayment(Long invoiceCode) {
        return payByInvoice(invoiceCode);
    }

    @Button
    @Override
    public boolean payByInvoice(Long invoiceCode) {
        Invoice invoice = findInvoiceByOrderNumber(invoiceCode);
        return !((invoice == null) || (invoice.getPaid()) || (invoice.getStatus() != InvoiceStatus.CREATED))
                && payByInvoice(invoice);
    }

    private synchronized boolean payByInvoice(Invoice invoice) {
        Set<String> products = invoice.getProducts().keySet();
        try {
            daoFactory.beginTransaction();
            paymentDao = daoFactory.getPaymentDao();
            invoiceDao = daoFactory.getInvoiceDao();
            transactionDao = daoFactory.getTransactionDao();
            for (String productCode : products) {
                Payment payment = invoice.getPayments().get(productCode);
                Transaction transaction = transactionService.createTransactionFromPayment(payment, invoice.getUserName(), TransactionType.PAYMENT);
                if (!transactionDao.addTransactionToDB(transaction)) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
            }
            invoice.setPaid(true);
            if (!invoiceDao.updateInvoiceInDB(invoice)) {
                daoFactory.rollbackTransaction();
                return false;
            }
            daoFactory.commitTransaction();
        } catch (DataBaseConnectionException ex) {
            log.error(ex);
            return false;
        }
        return true;
    }

    @Button
    @Override
    public boolean closeInvoice(Long invoiceCode) {
        Invoice invoice = findInvoiceByOrderNumber(invoiceCode);
        return !((invoice == null) || (invoice.getStatus() != InvoiceStatus.CREATED)) && closeInvoice(invoice);
    }

    private synchronized boolean closeInvoice(Invoice invoice) {
        Set<String> productCodes = invoice.getPayments().keySet();
        try {
            daoFactory.beginTransaction();
            productDao = daoFactory.getProductDao();
            paymentDao = daoFactory.getPaymentDao();
            invoiceDao = daoFactory.getInvoiceDao();
            for (String productCode : productCodes) {
                Product product = productDao.findProductByCode(productCode);
                Payment payment = paymentDao.findPaymentById(invoice.getPayments().get(productCode).getPaymentId());
                product.setReservedQuantity(product.getReservedQuantity() - payment.getQuantity());
                payment.setStatusId(InvoiceStatus.FINISHED);
                if ((!productDao.updateProductInDB(product)) || (!paymentDao.updatePaymentInDB(payment))) {
                    daoFactory.rollbackTransaction();
                    return false;
                }
            }
            invoice.setStatus(InvoiceStatus.FINISHED);
            if (!invoiceDao.updateInvoiceInDB(invoice)) {
                daoFactory.rollbackTransaction();
                return false;
            }
            daoFactory.commitTransaction();
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            log.error(ex);
            return false;
        }
        return true;
    }

    /** Private class methods */

    /** Invoice data integrity validator */

    @Button
    public Invoice createInvoiceFromUserCart(UserCart userCart) throws InvoiceServiceException {
        return createInvoiceFromUserCart(userCart, System.currentTimeMillis());
    }

    @Override
    public Invoice createInvoiceFromUserCart(UserCart userCart, Long orderCode) throws InvoiceServiceException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceCode(orderCode);
        invoice.setUserName(userCart.getUserName());
        invoice.setPaid(false);
        invoice.setStatus(InvoiceStatus.CREATED);
        invoice.setInvoiceNotes(userCart.getOrderNotes());
        for (Map.Entry<String, Double> unit : userCart.getProducts().entrySet()) {
            Product product;
            try {
                product = productService.findProductByCode(unit.getKey());
            } catch (ProductServiceException ex) {
                throw new InvoiceServiceException("Product " + unit.getKey() + " not found in DB");
            }
            invoice.addProduct(product);
            Payment payment = new Payment();
            payment.setOrderCode(orderCode);
            payment.setProductCode(unit.getKey());
            payment.setQuantity(unit.getValue());
            payment.setStatusId(InvoiceStatus.CREATED);
            payment.setPaymentNotes(userCart.getOrderNotes());
            invoice.addPayment(payment);
        }
        return invoice;
    }

    public UserCartView createUsersCartView(UserCart userCart) throws InvoiceServiceException {
        UserCartView view = new UserCartView(userCart.getUserName());
        Double cost = 0d;
        view.setOrderNotes(userCart.getOrderNotes());
        try {
            for (Map.Entry<String, Double> unit : userCart.getProducts().entrySet()) {
                Product product = productService.findProductByCode(unit.getKey());
                view.addProduct(product, unit.getValue());
                cost += product.getCost() * unit.getValue();
            }
            view.setTotalCost(cost);
        } catch (ProductServiceException pse) {
            log.error(pse);
            throw new InvoiceServiceException();
        }
        return view;
    }

    private boolean validateInvoice(Invoice invoice) {
        if ((invoice.getInvoiceCode() == null)
            || (invoice.getPayments().size() == 0)
            || (invoice.getProducts().size() == 0)
            || (invoice.getPayments().size() != invoice.getProducts().size()))
            return false;
        Long orderCode = invoice.getInvoiceCode();
        InvoiceStatus status = invoice.getStatus();
        for (Map.Entry<String, Payment> paymentEntry : invoice.getPayments().entrySet())
            if (!paymentEntry.getValue().getOrderCode().equals(orderCode)
                    || (paymentEntry.getValue().getStatusId() != status))
                return false;
        return true;
    }

    private synchronized boolean removePayment(Payment payment) {
        try {
            daoFactory.beginTransaction();
            paymentDao = daoFactory.getPaymentDao();
            productDao = daoFactory.getProductDao();
            Double quantity = payment.getQuantity();
            Product product = productDao.findProductByCode(payment.getProductCode());
            product.setQuantity(product.getQuantity() + quantity);
            product.setReservedQuantity(product.getReservedQuantity() - quantity);
            if ((!paymentDao.deletePaymentFromDB(payment))
            || (!productDao.updateProductInDB(product))) {
                daoFactory.rollbackTransaction();
                return false;
            }
            daoFactory.commitTransaction();
            return true;
        } catch (DataBaseConnectionException | DataNotFoundException ex) {
            return false;
        }
    }

    private synchronized boolean addPaymentsToInvoice(Invoice invoice, IPaymentDao paymentDao, IProductDao productDao) {
        List<Payment> payments;
        try {
            payments = paymentDao.findAllPaymentsByOrderCode(invoice.getInvoiceCode());
            if (payments == null)
                return false;
            for (Payment payment : payments) {
                String productCode = payment.getProductCode();
                Product product = productDao.findProductByCode(productCode);
                invoice.addProduct(product);
                invoice.addPayment(payment);
            }
        } catch (DataNotFoundException ex) {
            return false;
        }
        return true;
    }

    private synchronized Double calculateTotalCost(Invoice invoice) {
        Double result = 0d;
        for (Map.Entry<String, Payment> paymentEntry : invoice.getPayments().entrySet())
            result += paymentEntry.getValue().getPaymentValue();
        return result;
    }
}