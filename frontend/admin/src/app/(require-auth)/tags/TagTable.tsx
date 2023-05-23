"use client";

import NextLink from "next/link";
import {
  Link,
  Table,
  TableContainer,
  Tbody,
  Td,
  Th,
  Thead,
  Tr,
  HStack,
  Button,
} from "@chakra-ui/react";
import { EditIcon, DeleteIcon } from "@chakra-ui/icons";
import { type Tag } from "@/lib/api";

export default function TagTable({ tags }: { tags: Array<Tag> }) {
  return (
    <TableContainer>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>id</Th>
            <Th>emoji</Th>
            <Th>message</Th>
            <Th>category</Th>
          </Tr>
        </Thead>
        <Tbody>
          {tags.map((tag) => (
            <Tr key={tag.id}>
              <Td>{tag.id}</Td>
              <Td>{tag.emoji}</Td>
              <Td>{tag.message}</Td>
              <Td>{tag.category}</Td>
              <Td>
                <HStack gap={3}>
                  <Link
                    as={NextLink}
                    href={`/tags/edit/${tag.id}`}
                    color="teal"
                  >
                    <Button colorScheme="teal" size="sm">
                      <EditIcon />
                    </Button>
                  </Link>
                  <Button colorScheme="red" size="sm">
                    <DeleteIcon />
                  </Button>
                </HStack>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    </TableContainer>
  );
}
