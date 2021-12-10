import com.epam.project.domain.Invoice;
import com.epam.project.domain.Transaction;
import com.epam.project.exceptions.TransactionServiceException;
import com.epam.project.service.IInvoiceServ;
import com.epam.project.service.ITransactionServ;
import com.epam.project.service.ServiceFactory;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TransactionServiceTest {

    private static final Logger log = Logger.getLogger(TransactionServiceTest.class);
    private static Transaction transaction;
    private static Invoice invoice;
    private static IInvoiceServ invoiceService;
    private static ITransactionServ transactionService;

    @BeforeClass
    public static void init() throws TransactionServiceException {
        invoiceService = ServiceFactory.getInvoiceService();
        transactionService = ServiceFactory.getTransactionService();
        invoice = invoiceService.findInvoiceByOrderNumber(1L);
    }

    @AfterClass
    public static void destroy() {
        invoiceService = null;
        invoice = null;
        System.gc();
    }

    @Test
    public void findTransactionsTest() throws TransactionServiceException {
        List<Transaction> transactions = transactionService.findAllTransactions();
        log.info(transactions);
        assertTrue(transactions.size() > 0);
    }
}
