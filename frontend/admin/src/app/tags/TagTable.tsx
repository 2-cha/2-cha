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
} from "@chakra-ui/react";
import { type Tag } from "./page";

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
                <Link as={NextLink} href={`/tags/edit/${tag.id}`} color='teal'>
                  edit
                </Link>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    </TableContainer>
  );
}
