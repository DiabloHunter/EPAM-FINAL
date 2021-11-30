package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;

import org.apache.log4j.Logger;

public class CommandOpenAddNewUserAsAdmin implements ICommand {
    private static final Logger log = Logger.getLogger(CommandOpenAddNewUserAsAdmin.class);
    @Override
    public ExecutionResult execute(SessionRequestContent request) {
        ExecutionResult result = new ExecutionResult();
        result.setPage(Configuration.getInstance().getPage("openAddNewUserAsAdminPage"));
        result.setDirection(Direction.FORWARD);
        return result;
    }
}
